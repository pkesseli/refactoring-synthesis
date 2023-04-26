import os
import sys
from revChatGPT.V3 import Chatbot
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
    chatbot = Chatbot(
        api_key=os.getenv('OPENAI_API_KEY'),
        engine='gpt-3.5-turbo',
        temperature=0.0,
    )
    with open(os.path.join(entry, 'query.txt'), 'r') as file:
        query = file.read()

    with open(os.path.join(entry, 'response.txt'), 'w') as file:
        while True:
            try:
                start = time()
                response = ""
                response = chatbot.ask(query)
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
