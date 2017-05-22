import re
import random
from collections import OrderedDict

input_output_json = OrderedDict()


input_output_json['thank you .*'] =  ['I am glad I can help', 'I am happy I can help']
input_output_json['thank you'] = ['I am glad to be of your service']
input_output_json['what is your name.*'] = ['My name is Eliza', 'Eliza']
input_output_json['who created you (.*)'] = ['Joseph Weizenbaum']
input_output_json['who made you (.*)'] = ['Joseph Weizenbaum']
input_output_json['what are you doing(.*)'] = ['I am talking with you']
input_output_json['when were you born(.*)'] = ['I was born between 1964 to 1966']
input_output_json['how old are you(.*)'] =['I am in my forties']
input_output_json['no(.*)'] = ['OK, I didn\'t think so.']
input_output_json['yes(.*)'] = ['Are you sure?', 'Ok, I agree with you']
input_output_json['well(.*)'] =['You were saying...']
input_output_json['why(.*)'] = ['Well, first tell me what you think.']
input_output_json['what(.*)'] = ['What do you think?', 'Good question. Anything else I can do for you']
input_output_json['where am i(.*)'] = ['In New York', 'In the United States']
input_output_json['where (.*)' ] = ['It should be nearby', 'Look around', 'What do you think']
input_output_json['when(.*)'] = ['When, indeed?', 'Perhaps in the near future']
input_output_json['talking (.*) you (.*)'] = ['Yes you are', 'Yes tell me more']
input_output_json['ok(.*)'] = ['Tell me more', 'I see']
input_output_json['is there (.*)'] = ['Do you think so?']
input_output_json['i feel (.*)'] = ['Do often feel that way?']
input_output_json['i want (.*)'] = ['Why?']
input_output_json['i am (.*)'] = ['Is that what you want to be doing?']
input_output_json['i think (.*)'] = ['I agree with you', 'I don\'t agree with what you are saying', 'Sounds interesting']
input_output_json['i (.*)'] = ['Tell me more', 'I understand']
input_output_json['hello(.*)'] = ['Hi, how are you?', 'Hi']
input_output_json['hi(.*)'] = ['Hi, what are you doing?', 'Greetings!']
input_output_json['hey(.*)'] = ['Hey']
input_output_json['.*'] = [   'Why do you say that?',
                'How do you feel when you say that?',
                'Very Interesting',
                'Do you feel strongly about that?',
                'Hmm I understand',
                'Is everything ok?',
                'That makes sense, tell me more',
                'Ok... anything else you want to add?',
            ]


def chat_response(human):
    human = human.lower()
    for key in input_output_json.keys():
        if re.match(key, human):
            arr = input_output_json.get(key)
            return random.choice(arr)
    return random.choice(input_output_json.get(''))