from json import dumps

from flask import Flask, request

from parser_classes import ParserShop, ParserForcecom, ParserTomas
from config import URLS

app = Flask(__name__)


@app.route('/', methods=['POST'])
def start_parsing():
    parsing_parameters = request.get_json(force=True)

    hardware = parsing_parameters['hardware']

    use_shop = parsing_parameters['useShop']
    use_forcecom = parsing_parameters['useForcecom']
    use_tomas = parsing_parameters['useTomas']

    data_json = {
        "shop": None,
        "forcecom": None,
        "tomas": None
    }

    if use_shop:
        if hardware == 'Graphic cards':
            parser = ParserShop(URLS['SHOP_GRAPHIC_CARDS_URL'])
            data_json['shop'] = parser.get_data()
        elif hardware == 'CPU Processors':
            parser = ParserShop(URLS['SHOP_CPU_PROCESSORS_URL'])
            data_json['shop'] = parser.get_data()
        elif hardware == 'Motherboards':
            parser = ParserShop(URLS['SHOP_MOTHERBOARDS_URL'])
            data_json['shop'] = parser.get_data()

    if use_forcecom:
        if hardware == 'Graphic cards':
            parser = ParserForcecom(URLS['FORCECOM_GRAPHIC_CARDS_URL'])
            data_json['forcecom'] = parser.get_data()
        elif hardware == 'CPU Processors':
            parser = ParserForcecom(URLS['FORCECOM_CPU_PROCESSORS_URL'])
            data_json['forcecom'] = parser.get_data()
        elif hardware == 'Motherboards':
            parser = ParserForcecom(URLS['FORCECOM_MOTHERBOARDS_URL'])
            data_json['forcecom'] = parser.get_data()

    if use_tomas:
        if hardware == 'Graphic cards':
            parser = ParserTomas(URLS['TOMAS_GRAPHIC_CARDS_URL'])
            data_json['tomas'] = parser.get_data()
        elif hardware == 'CPU Processors':
            parser = ParserTomas(URLS['TOMAS_CPU_PROCESSORS_URL'])
            data_json['tomas'] = parser.get_data()
        elif hardware == 'Motherboards':
            parser = ParserTomas(URLS['TOMAS_MOTHERBOARDS_URL'])
            data_json['tomas'] = parser.get_data()

    return dumps(data_json)


if __name__ == '__main__':
    app.run(host="192.168.1.22", debug=True)
