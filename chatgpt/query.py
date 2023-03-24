import os
import sys
from revChatGPT.V1 import Chatbot
from time import time, sleep
import itertools

queries_dir = sys.argv[1]

# entries = os.listdir(queries_dir)

for entry in range(sys.maxsize):
    entry = str(entry)
    entry = os.path.join(queries_dir, entry)
    if not os.path.exists(entry):
        break
    print('Querying ' + os.path.basename(entry))
    chatbot = Chatbot(config={
        "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJwZDIxNTQxQGJyaXN0b2wuYWMudWsiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZX0sImh0dHBzOi8vYXBpLm9wZW5haS5jb20vYXV0aCI6eyJ1c2VyX2lkIjoidXNlci1JdERMY2U3ZHFSMDR5c3NvWHo0WDlxVlkifSwiaXNzIjoiaHR0cHM6Ly9hdXRoMC5vcGVuYWkuY29tLyIsInN1YiI6ImF1dGgwfDY0MDFlN2E0ZmE3NjY5ZDI0YzBiYjM4MiIsImF1ZCI6WyJodHRwczovL2FwaS5vcGVuYWkuY29tL3YxIiwiaHR0cHM6Ly9vcGVuYWkub3BlbmFpLmF1dGgwYXBwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE2Nzk0OTQ3ODIsImV4cCI6MTY4MDcwNDM4MiwiYXpwIjoiVGRKSWNiZTE2V29USHROOTVueXl3aDVFNHlPbzZJdEciLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIG1vZGVsLnJlYWQgbW9kZWwucmVxdWVzdCBvcmdhbml6YXRpb24ucmVhZCBvZmZsaW5lX2FjY2VzcyJ9.jZwfcKuTS2bLfErakTn2JL7fqp_IYzFL9IHHHACpDMOVMSBgUDky7dOZZvWRyFDLOFOyH0aaXip93tJKNMr-yDbh8aOSs1ENCvhnakMIlkjOfS19ovP92ZT8IMM9uvhGyTiviJM7Rn36q1t6WiZfwsIfp5K-mr1HCumLvgPAO354GBjrBQr9V5wPDE5Hw8ie9d8Fv70dCqpiVHhnr707rOUbJI1vUVc9TLltTfMaXkcyBvqzUKu2DzSDJS3Sj1adr6g_Yup4IUMg-JTmdPRpif36cyMipr69ARKbnZqd2zaXbon0H-C-Opp_GaPnYAq6f5eC-71rEoBNGaaZueskAA"
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
                file.write('# Response Time: ' + str(elapsed) + '\n')
                file.write(response)
                # chatbot.rollback_conversation()
                break
            except Exception as e:
                print('Retry after 10 minutes..')
                sleep(600)
                continue
                # if e.code != 2:
                #     raise
                # else:
                #     print('Rate limit exceeded, waiting for one hour..')
                #     sleep(3600)
                #     continue
