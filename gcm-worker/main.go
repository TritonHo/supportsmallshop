package main

import (
	"bufio"
	"github.com/garyburd/redigo/redis"
	"github.com/googollee/go-gcm"
	"log"
	"os"
	"strings"
)

//global variable
var conn redis.Conn
var client *gcm.Client

func readLineFromFile(filePath string) (string, error) {
	file, err := os.Open(filePath)
	if err != nil {
		return "", err
	}
	scanner := bufio.NewScanner(file)
	scanner.Scan()
	output := scanner.Text()
	file.Close()

	return output, nil
}

func sendMessage(redisMessage string) {
	//the message must be playerId:JSON_body
	temp := strings.SplitN(redisMessage, ":", 2)

	messageBody := temp[0]
	regId := temp[1]

	//send message to google
	load := gcm.NewMessage()
	load.DelayWhileIdle = false
	load.SetPayload("data", messageBody)
	load.AddRecipient(regId)

	_, err := client.Send(load)
	if err != nil {
		log.Fatal("sendMessage gcm error: %s", err)
	}
	//TODO: maybe take care the resp.RefreshIndexes() some day later
}

func main() {
	redisUrl, err := readLineFromFile("/etc/supportsmallshop/redis.txt")
	if err != nil {
		log.Panic("Cannot read /etc/supportsmallshop/redis.txt")
	}
	gcmKey, err := readLineFromFile("/etc/questionator/google_gcm_key.txt")
	if err != nil {
		log.Panic("Cannot read /etc/supportsmallshop/google_gcm_key.txt")
	}
	client = gcm.New(gcmKey)

	//connect to redis database
	conn, err = redis.Dial("tcp", redisUrl)
	if err != nil {
		log.Fatal("could not connect to redis: %s", err)
	}
	defer conn.Close()

	//clear the history
	message_array, err := redis.Strings(conn.Do("LRANGE", `gcm_auth_code_list_working`, 0, 1000))
	if err != nil {
		log.Fatal("problem on getting gcm_auth_code_list_working: %s", err)
	}
	for _, message := range message_array {
		sendMessage(message)
		conn.Do("LPOP", `gcm_auth_code_list_working`)
	}

	//use blocking IO, while(1) loop
	for {
		message, err := redis.String(conn.Do("BRPOPLPUSH", `gcm_auth_code_list`, `gcm_auth_code_list_working`, 0))
		if err != nil {
			log.Fatal("problem on getting gcm_auth_code_list: %s", err)
		}
		sendMessage(message)
		conn.Do("LPOP", `gcm_auth_code_list`)
	}
}
