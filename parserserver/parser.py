from abc import ABC, abstractmethod
import ast
import requests

import bs4
from bs4 import BeautifulSoup


class Parser(ABC):
    def __init__(self, url):
        self.url = url

    @staticmethod
    def get_html(url, params=None):
        html = requests.get(url,
                            headers={'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
                                                   '(KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36',
                                     'accept': '*/*'},

                            params=params)

        return html

    @abstractmethod
    def get_pages_count(self):
        pass

    @abstractmethod
    def get_data(self):
        for page in range(1, self.get_pages_count() + 1):
            pass


class ParserShop(Parser):
    def __init__(self, url):
        Parser.__init__(self, url)
        self.soup_pagination = BeautifulSoup(self.get_html(self.url).text, 'html.parser', parse_only=bs4.SoupStrainer(
            'div', {'class': 'bx-pagination-container row'}))

    def get_pages_count(self):
        pagination = self.soup_pagination.find('div', class_='bx-pagination-container row').find_all('li', class_='')
        pages_count = int(pagination[-1].get_text())

        return pages_count

    def get_data(self):
        data = []

        for page in range(1, self.get_pages_count() + 1):
            html = self.get_html(self.url, params={'PAGEN_1': page})
            soup = BeautifulSoup(html.text, 'html.parser',
                                 parse_only=bs4.SoupStrainer('div', {'class': 'bx_catalog_item double'}))

            items = soup.find_all('div', class_='bx_catalog_item double')

            for item in items:
                data.append({

                    'image_url': 'https:' + item.find('div', class_='bx_catalog_item_container gtm-impression-product'
                                                      ).find('figure', class_='item_image_container').find('a')['style']
                    [23:-2],


                    'title': ast.literal_eval(
                        item.find('div', class_='bx_catalog_item_container gtm-impression-product')['data-product'])[
                        'name'],

                    'description': '',

                    'cost': ast.literal_eval(
                        item.find('div', class_='bx_catalog_item_container gtm-impression-product')['data-product'])[
                        'price'],

                    'url': 'https://shop.kz' + item.find('div',
                                                         class_='bx_catalog_item_container gtm-impression-product').find
                    ('div', class_='bx-catalog-middle-part').find('div', class_='bx_catalog_item_title').find('a')
                    ['href']

                })

                description_spans = item.find('div',
                                              class_='bx_catalog_item_container gtm-impression-product').find_all(
                    'span')

                for description_span in description_spans:
                    data[-1]['description'] = data[-1]['description'] + description_span.get_text() + ' '

        return data


class ParserForcecom(Parser):
    def __init__(self, url):
        Parser.__init__(self, url)
        self.soup_pagination = BeautifulSoup(self.get_html(self.url).text, 'html.parser',
                                             parse_only=bs4.SoupStrainer('div', {
                                                 'class': 'nums'}))

    def get_pages_count(self):
        pagination = self.soup_pagination.find('div',
                                               class_='nums').find_all('a')
        pages_count = int(pagination[-1].get_text())

        return pages_count

    def get_data(self):
        data = []

        for page in range(1, self.get_pages_count() + 1):
            html = self.get_html(self.url, params={'PAGEN_1': page})

            soup = BeautifulSoup(html.text, 'html.parser',
                                 parse_only=bs4.SoupStrainer('div', {'class': 'list_item_wrapp item_wrap item'}))

            items = soup.find_all('div', class_='list_item_wrapp item_wrap item')

            for item in items:
                data.append({

                    'image_url': 'https://' + 'forcecom.kz' + item.find('table').find('tr', class_='').find
                    ('td', class_='image_block').find('div', class_='image_wrapper_block').find('a').find('img')['src'],

                    'title': item.find('table').find('tr', class_='').find('td', class_='description_wrapp').find(
                        'div', class_='description').find('div', class_='item-title').find('a').find('span').get_text()
                    [10:],

                    'description': None,

                    'cost': None,

                    'url': 'https://forcecom.kz' + item.find('table').find('tr', class_='').find
                    ('td', class_='image_block').find('div', class_='image_wrapper_block').find('a')['href']

                })

                try:
                    d = item.find('table').find('tr', class_='').find('td', class_='description_wrapp').find(
                        'div', class_='description').find('div', class_='preview_text').get_text()

                    data[-1]['description'] = d[0]  # d[1:-1]
                except AttributeError:
                    pass

                try:
                    data[-1]['cost'] = int(item.find('table').find('tr', class_='').find
                                           ('td', class_='information_wrapp main_item_wrapper').find
                                           ('div', class_='information').find('div', class_='cost prices clearfix').find
                                           ('div', class_='price_group 1497a90b-3ddf-11e6-89ef-ac9e1788bb3d').find
                                           ('div', class_='price_matrix_wrapper').find('div', class_='price')
                                           ['data-value'])

                except AttributeError:
                    pass

        return data


class ParserTomas(Parser):
    def __init__(self, url):
        Parser.__init__(self, url)
        self.soup_pagination = BeautifulSoup(self.get_html(self.url).text, 'html.parser',
                                             parse_only=bs4.SoupStrainer('div', {
                                                 'class': 'pager pager_goods'}))

    def get_pages_count(self):
        pagination = self.soup_pagination.find('div', class_='pager pager_goods').find_all('a')
        pages_count = int(pagination[-1].get_text())

        return pages_count

    def get_data(self):
        data = []

        for page in range(1, 2):
            html = self.get_html(self.url + str(page))
            soup = BeautifulSoup(html.text, 'html.parser', parse_only=bs4.SoupStrainer('div', {'class': 'goods__item'}))

            items = soup.find_all('div', class_='goods__item')

            for item in items:
                data.append({

                    'image_url': 'http s:' +
                                 item.find('div', class_='goods__img-row').find('a').find('span').find('img')['src'],

                    'title': item.find('div', class_='goods__img-row').find('a')['title'],

                    'description': None,

                    'cost': item.find('div', class_='goods__price-row').find('div').find('div').find('span').get_text(
                        strip=True)[:-2],

                    'url': item.find('div', class_='goods__img-row').find('a')['href'],

                })

                try:
                    data[-1]['cost'] = int(float(data[-1]['cost'].replace(u'\xa0', u'').replace(',', '.')))
                except ValueError:
                    data[-1]['cost'] = item.find('div', class_='goods__price-row').find('div').find('div').find(
                        'span').find('span').get_text(strip=True)[:-2]
                    data[-1]['cost'] = int(float(data[-1]['cost'].replace(u'\xa0', u'').replace(',', '.')))

        return data
