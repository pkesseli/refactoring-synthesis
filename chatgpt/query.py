import os
import sys
from revChatGPT.V1 import Chatbot, Error
from time import time, sleep
import itertools

# chatbot = Chatbot(config={
#     "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJwZDIxNTQxQGJyaXN0b2wuYWMudWsiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZ2VvaXBfY291bnRyeSI6IkdCIn0sImh0dHBzOi8vYXBpLm9wZW5haS5jb20vYXV0aCI6eyJ1c2VyX2lkIjoidXNlci1JdERMY2U3ZHFSMDR5c3NvWHo0WDlxVlkifSwiaXNzIjoiaHR0cHM6Ly9hdXRoMC5vcGVuYWkuY29tLyIsInN1YiI6ImF1dGgwfDY0MDFlN2E0ZmE3NjY5ZDI0YzBiYjM4MiIsImF1ZCI6WyJodHRwczovL2FwaS5vcGVuYWkuY29tL3YxIiwiaHR0cHM6Ly9vcGVuYWkub3BlbmFpLmF1dGgwYXBwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE2Nzc4NDY0OTAsImV4cCI6MTY3OTA1NjA5MCwiYXpwIjoiVGRKSWNiZTE2V29USHROOTVueXl3aDVFNHlPbzZJdEciLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIG1vZGVsLnJlYWQgbW9kZWwucmVxdWVzdCBvcmdhbml6YXRpb24ucmVhZCBvZmZsaW5lX2FjY2VzcyJ9.1YPOAiD6UekoubJuPd3opWpTnWcSQ5LY8-5edvsAcyynXZ2sAXJzBSAm_c6FsK1siQ6jc70zmgW_FJFuPwQy7ovv02Jd1n3TYEvZm1BN21BlW6_PBPrZx9EePVQyOIwjqHE2ZjQTjX8EiTTXZj5y14AG4ZkQWE7XiTeYq6VYfgHT4G9GQm-r5CWb9MRb9FX0yjUu9b5JWszUsnFhVLYiPCNZ5u2YMqaHw8MmZizsxy0Tr9tZAESxkRc9KXdViphcu5GXMcHSpZPhhYs0eqqGt8sDLEqsJ8araNRxF2Yj78aA7-yekAQAdG2O1q80KmnnxFL5ZslNNLOs9WSjmgZhTQ"
# })

queries_dir = sys.argv[1]

# entries = os.listdir(queries_dir)

for entry in range(sys.maxsize):
    entry = str(entry)
    entry = os.path.join(queries_dir, entry)
    if not os.path.exists(entry):
        break
    print('Querying ' + os.path.basename(entry))
    chatbot = Chatbot(config={
        "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJwZDIxNTQxQGJyaXN0b2wuYWMudWsiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZ2VvaXBfY291bnRyeSI6IkdCIn0sImh0dHBzOi8vYXBpLm9wZW5haS5jb20vYXV0aCI6eyJ1c2VyX2lkIjoidXNlci1JdERMY2U3ZHFSMDR5c3NvWHo0WDlxVlkifSwiaXNzIjoiaHR0cHM6Ly9hdXRoMC5vcGVuYWkuY29tLyIsInN1YiI6ImF1dGgwfDY0MDFlN2E0ZmE3NjY5ZDI0YzBiYjM4MiIsImF1ZCI6WyJodHRwczovL2FwaS5vcGVuYWkuY29tL3YxIiwiaHR0cHM6Ly9vcGVuYWkub3BlbmFpLmF1dGgwYXBwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE2Nzc4NDY0OTAsImV4cCI6MTY3OTA1NjA5MCwiYXpwIjoiVGRKSWNiZTE2V29USHROOTVueXl3aDVFNHlPbzZJdEciLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIG1vZGVsLnJlYWQgbW9kZWwucmVxdWVzdCBvcmdhbml6YXRpb24ucmVhZCBvZmZsaW5lX2FjY2VzcyJ9.1YPOAiD6UekoubJuPd3opWpTnWcSQ5LY8-5edvsAcyynXZ2sAXJzBSAm_c6FsK1siQ6jc70zmgW_FJFuPwQy7ovv02Jd1n3TYEvZm1BN21BlW6_PBPrZx9EePVQyOIwjqHE2ZjQTjX8EiTTXZj5y14AG4ZkQWE7XiTeYq6VYfgHT4G9GQm-r5CWb9MRb9FX0yjUu9b5JWszUsnFhVLYiPCNZ5u2YMqaHw8MmZizsxy0Tr9tZAESxkRc9KXdViphcu5GXMcHSpZPhhYs0eqqGt8sDLEqsJ8araNRxF2Yj78aA7-yekAQAdG2O1q80KmnnxFL5ZslNNLOs9WSjmgZhTQ"
    })
    with open(os.path.join(entry, 'query.txt'), 'r') as file:
        query = file.read()

    with open(os.path.join(entry, 'response.txt'), 'w') as file:
        while True:
            try:
                start = time()
                response = ""
                for data in chatbot.ask(
                    query,
                ):
                    response = data["message"]
                elapsed = time() - start
                file.write('# Response Time: ' + str(elapsed))
                file.write(response)
                # chatbot.rollback_conversation()
            except Error as e:
                if e.code != 2:
                    raise
                else:
                    print('Rate limit exceeded, waiting for one hour..')
                    sleep(3600)
                    continue