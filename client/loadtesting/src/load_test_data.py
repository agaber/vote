from typing import List
import json
import requests

BASE_URL = 'http://localhost:8080/vote/api/v1/elections'
# BASE_URL = 'https://apis.agaber.dev/vote/api/v1/elections'
HEADERS =  {"Content-Type":"application/json"}


class ElectionService:

  def create_election(self, question: str, options: List[str]):
    election = {'question': question, 'options': options}
    request_body = json.dumps(election);
    response = requests.post(BASE_URL, request_body, headers=HEADERS)
    if response.status_code != 200:
      print(request_body)
      print(response)
      raise Exception('Something went wrong when creating an election!')
    return response

  def vote(self, electionId: str, choices: List[str]):
    url = f'{BASE_URL}/{electionId}:vote'
    request_body = json.dumps({'choices': choices})
    response = requests.post(url, request_body, headers=HEADERS)
    if (response.status_code != 200):
      print(request_body)
      print(response)
      raise Exception('Something went wrong when submitting a vote!')
    return response


# Load CGP Grey's animal kingdom election.
def load_animal_kindom_data():
  es = ElectionService()

  create_election_response = es.create_election(
      'Who should rule the animal kingdom?',
      ['Turtle', 'Gorilla', 'Owl', 'Leopard', 'Tiger'])

  electionId = create_election_response.json()['id']
  print(f'Created election with id {electionId}')

  es.vote(electionId, ["Turtle", "Owl", "Gorilla", "Tiger", "Leopard"])
  es.vote(electionId, ["Gorilla", "Owl", "Tiger", "Leopard", "Turtle"])
  es.vote(electionId, ["Gorilla", "Owl"])
  es.vote(electionId, ["Gorilla", "Owl"])
  es.vote(electionId, ["Gorilla", "Owl"])
  es.vote(electionId, ["Gorilla", "Owl"])
  es.vote(electionId, ["Owl"])
  es.vote(electionId, ["Owl"])
  es.vote(electionId, ["Owl"])
  es.vote(electionId, ["Owl"])
  es.vote(electionId, ["Owl"])
  es.vote(electionId, ["Leopard"])
  es.vote(electionId, ["Leopard"])
  es.vote(electionId, ["Leopard"])
  es.vote(electionId, ["Leopard"])
  es.vote(electionId, ["Leopard"])
  es.vote(electionId, ["Leopard"])
  es.vote(electionId, ["Tiger", "Leopard"])
  es.vote(electionId, ["Tiger", "Leopard"])
  es.vote(electionId, ["Tiger", "Leopard"])
  print('Done')


if __name__ == '__main__':
  load_animal_kindom_data()
