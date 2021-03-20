import requests
import json

from flask import Flask, jsonify, request

from bs4 import BeautifulSoup

HEADERS = {
    'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/'
                  '85.0.4183.121 Safari/537.36',

    'accept': '*/*'}


def get_html(url, params=None):
    r = requests.get(url, headers=HEADERS, params=params)

    return r


url = 'https://shop.kz/protsessory/filter/karaganda-is-v_nalichii-or-ojidaem-or-dostavim/apply/'


def parse():
    data = []

    html = get_html(url)

    soup = BeautifulSoup(html.text, 'lxml')

    element_class = 'bx_catalog_item double'

    items = soup.find_all('div', class_=element_class)

    for item in items:
        data.append(
            {

                'title': item.find('div', class_='bx-catalog-middle-part').find
                ('div', class_='bx_catalog_item_title').find('a').get_text(strip=True)


            }
        )

    return data


app = Flask(__name__)


@app.route('/get')
def get_data():
    data = parse()

    return jsonify({'data': data})


@app.route('/post', methods=['POST'])
def post_data():
    data = request.get_json(force=True)

    return data


if __name__ == '__main__':
    app.run(host="192.168.1.22", debug=True)
