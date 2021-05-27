from bs4 import BeautifulSoup, SoupStrainer

import requests
import json

HEADERS = {
    'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) '
                  'Chrome/85.0.4183.121 Safari/537.36', 'accept': '*/*'}


def get_html(url, params=None):
    r = requests.get(url, headers=HEADERS, params=params)

    return r


def parse_shop():
    data = []
    url = 'https://shop.kz/materinskie-platy/filter/karaganda-is-v_nalichii-or-ojidaem-or-dostavim/apply/'

    def get_pages_count():
        html = get_html(url)
        soup = BeautifulSoup(html.text, 'html.parser', parse_only=SoupStrainer('div', {'class': 'bx-pagination-container row'}))

        pagination = soup.find('div', class_='bx-pagination-container row').find_all('li', class_='')

        return int(pagination[-1].get_text())

    for page in range(1, get_pages_count() + 1):
        html = get_html(url, params={'PAGEN_1': page})
        soup = BeautifulSoup(html.text, 'html.parser', parse_only=SoupStrainer('div', {'class': 'bx_catalog_item_container gtm-impression-product'}))

        items = soup.find_all('div', class_='bx_catalog_item_container gtm-impression-product')

        for item in items:
            data.append({

                'image_url': 'https:' + item.find('figure', class_='item_image_container').find('a')['style'][23:-2],

                'title': item.find('div', class_='bx-catalog-middle-part').find
                ('div', class_='bx_catalog_item_title').find('a').get_text()[18:],

                'description': '',

                'cost': item.find('div', class_='bx-catalog-right-part').find
                ('div', class_='bx_catalog_item_price').find('div').find('div', class_='bx-more-prices').find
                ('ul').find_all('li')[-1].find('span', class_='bx-more-price-text').get_text(),

                'url': 'https://shop.kz' + item.find('div', class_='bx-catalog-middle-part').find
                ('div', class_='bx_catalog_item_title').find('a')['href']

            })

            description_spans = item.find_all('span')

            for description_span in description_spans:
                data[-1]['description'] = data[-1]['description'] + description_span.get_text() + ' '

    return data


def parse_forcecom():
    data = []
    url = 'https://forcecom.kz/catalog/motherboards/'

    def get_pages_count():
        html = get_html(url)
        soup = BeautifulSoup(html.text, 'html.parser', parse_only=SoupStrainer('div', {'class': 'nums'}))

        pagination = soup.find('div', class_='nums').find_all('a')

        return int(pagination[-1].get_text())

    for page in range(1, get_pages_count() + 1):
        html = get_html(url, params={'PAGEN_1': page})
        soup = BeautifulSoup(html.text, 'html.parser', parse_only=SoupStrainer('div', {'class': 'list_item_wrapp item_wrap item'}))

        items = soup.find_all('div', class_='list_item_wrapp item_wrap item')

        for item in items:
            data.append({

                'image_url': 'https://' + 'forcecom.kz' +
                             item.find('table').find('tr', class_='').find('td', class_='image_block').find('div',
                                                                                                            class_='image_wrapper_block').find(
                                 'a').find('img')['src'],

                'title': item.find('table').find('tr', class_='').find('td', class_='description_wrapp').find('div',
                                                                                                              class_='description').find(
                    'div', class_='item-title').find('a').find('span').get_text()[18:],

                'description': None,

                'cost': None,

                'url': 'https://forcecom.kz' +
                       item.find('table').find('tr', class_='').find('td', class_='image_block').find('div',
                                                                                                      class_='image_wrapper_block').find(
                           'a')['href']

            })

            try:
                d = item.find('table').find('tr', class_='').find('td', class_='description_wrapp').find('div',
                                                                                                         class_='description').find(
                    'div', class_='preview_text').get_text(),
                data[-1]['description'] = d[0]  # d[1:-1]
            except AttributeError:
                pass

            try:
                data[-1]['cost'] = \
                item.find('table').find('tr', class_='').find('td', class_='information_wrapp main_item_wrapper').find(
                    'div', class_='information').find('div', class_='cost prices clearfix').find('div',
                                                                                                 class_='price_group 1497a90b-3ddf-11e6-89ef-ac9e1788bb3d').find(
                    'div', class_='price_matrix_wrapper').find('div', class_='price')['data-value']
            except AttributeError:
                pass

    return data


def parse_tomas():
    data = []
    url = 'https://tomas.kz/t/materinskie-platy-1266/'

    def get_pages_count():
        html = get_html(url)
        soup = BeautifulSoup(html.text, 'html.parser', parse_only=SoupStrainer('div', {'class': 'pager pager_goods'}))

        pagination = soup.find('div', class_='pager pager_goods').find_all('a')

        return int(pagination[-1].get_text())

    for page in range(1, 2):
        html = get_html(url + str(page))
        soup = BeautifulSoup(html.text, 'html.parser', parse_only=SoupStrainer('div', {'class': 'goods__item'}))

        items = soup.find_all('div', class_='goods__item')

        for item in items:
            data.append({

                'image_url': 'https:' + item.find('div', class_='goods__img-row').find('a').find('span').find('img')['src'],

                'title': item.find('div', class_='goods__img-row').find('a')['title'],

                'description': None,

                'cost': item.find('div', class_='goods__price-row').find('div').find('div').find('span').get_text(
                    strip=True),

                'url': item.find('div', class_='goods__img-row').find('a')['href'],

            })

    return data


def parse(use_shop, use_forcecom, use_tomas):

    data = {
        "shop": None,
        "forcecom": None,
        "tomas": None
    }

    if use_shop:
        data_shop = parse_shop()

        data['shop'] = data_shop

    if use_forcecom:
        data_forcecom = parse_forcecom()

        data['forcecom'] = data_forcecom

    if use_tomas:
        data_tomas = parse_tomas()

        data['tomas'] = data_tomas

    return data


