import os
import sys
from revChatGPT.V1 import Chatbot

chatbot = Chatbot(config={
    "email": input("Email: "),
    "password": input("Password: ")
})


entries = os.listdir('queries')

for entry in entries:
    entry = os.path.join('queries', entry)
    with open(os.path.join(entry, 'query.txt'), 'r') as file:
        query = file.read()

    original_stdout = sys.stdout
    with open(os.path.join(entry, 'response.txt'), 'w') as file:
        sys.stdout = file
        prev_text = ""
        for data in chatbot.ask(
            query,
        ):
            message = data["message"][len(prev_text) :]
            print(message, end="", flush=True)
            prev_text = data["message"]
        print()
        sys.stdout = original_stdout
