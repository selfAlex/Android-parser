from parse_graphiccards import parse as parse_graphiccards
from parse_cpu import parse as parse_cpu
from parse_motherboards import parse as parse_motherboards

from flask import Flask, request
from json import dumps

app = Flask(__name__)


@app.route('/', methods=['POST'])
def start_parsing():

    parsing_parameters = request.get_json(force=True)

    hardware = parsing_parameters['hardware']

    use_shop = parsing_parameters['useShop']
    use_forcecom = parsing_parameters['useForcecom']
    use_tomas = parsing_parameters['useTomas']

    if hardware == 'Graphic cards':
        data_json = parse_graphiccards(use_shop, use_forcecom, use_tomas)

    elif hardware == 'CPU Processors':
        data_json = parse_cpu(use_shop, use_forcecom, use_tomas)

    elif hardware == 'Motherboards':
        data_json = parse_motherboards(use_shop, use_forcecom, use_tomas)

    else:
        data_json = None

    return dumps(data_json)


def parse():
    pass


if __name__ == '__main__':
    app.run(host="192.168.1.22", debug=True)
