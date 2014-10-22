insert into submission_response_type(id, message, is_reject, is_serious_reject, is_accept) values
(1, '確定', false, false, true),
(2, '色情內容', false, true, false),
(3, '煽動暴力內容', false, true, false),
(4, '歧視內容', false, true, false),
(5, '資料錯誤', true, false, false),
(6, '地址錯誤', true, false, false),
(7, '電話錯誤', true, false, false),
(8, '小店介紹內含短期性資訊', true, false, false);
