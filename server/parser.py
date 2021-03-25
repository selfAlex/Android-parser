from parse_graphiccards import parse as parse_graphiccards
from parse_cpu import parse as parse_cpu
from parse_motherboards import parse as parse_motherboards

from flask import Flask, jsonify, request
from json import dumps

app = Flask(__name__)


@app.route('/get')
def get_data():
    data = '******'

    return jsonify({'data': data})


@app.route('/prikol', methods=['POST'])
def prikol():
    p = {'shop': [{'product': 'my_product', 'shop': 'my_shop'}], 'forcecom': 2, 'satu': 3}

    return dumps(p)


@app.route('/', methods=['POST'])
def start_parsing():

    parsing_parameters = request.get_json(force=True)

    hardware = parsing_parameters['hardware']

    use_shop = parsing_parameters['use_shop']
    use_forcecom = parsing_parameters['use_forcecom']
    use_satu = parsing_parameters['use_satu']

    if hardware == 'Graphic cards':
        data_json = parse_graphiccards(use_shop, use_forcecom, use_satu)

    elif hardware == 'CPU Processors':
        data_json = parse_cpu(use_shop, use_forcecom, use_satu)

    elif hardware == 'Motherboards':
        data_json = parse_motherboards(use_shop, use_forcecom, use_satu)

    else:
        data_json = None

    return dumps(data_json)


def parse():
    pass


if __name__ == '__main__':
    app.run(host="192.168.1.22", debug=True)
