import requests

from bs4 import BeautifulSoup

HEADERS = {
    'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/'
                  '85.0.4183.121 Safari/537.36',

    'accept': '*/*'}


def get_html(url, params=None):
    r = requests.get(url, headers=HEADERS, params=params)

    return r


def get_pages_count(html):
    pass


def define_urls(hardware):
    if hardware == 'CPU Processors':
        url_shop = "https://shop.kz/protsessory/filter/karaganda-is-v_nalichii-or-ojidaem-or-dostavim/apply/"
        url_technodom = "https://www.technodom.kz/karaganda/noutbuki-i-komp-jutery/komplektujuschie/processory"
        url_satu = "https://satu.kz/search?category=70701&search_term=процессоры&a5527=135299"

    elif hardware == 'Graphic cards':
        url_shop = "https://shop.kz/videokarty/filter/karaganda-is-v_nalichii-or-ojidaem-or-dostavim/apply/"
        url_technodom = "https://www.technodom.kz/karaganda/noutbuki-i-komp-jutery/komplektujuschie/videokarty"
        url_satu = "https://satu.kz/Videokarty?a5527=135299"

    elif hardware == 'Motherboards':
        url_shop = "https://shop.kz/materinskie-platy/filter/karaganda-is-v_nalichii-or-ojidaem-or-dostavim/apply/"
        url_technodom = "https://www.technodom.kz/karaganda/noutbuki-i-komp-jutery/komplektujuschie/materinskie-platy"
        url_satu = "https://satu.kz/Materinskie-platy?a5527=135299"

    else:
        raise Exception('define_urls() error')

    return url_shop, url_technodom, url_satu


def parse_shop(url_shop):
    data = []
    html = get_html(url_shop)

    for page in range(1, get_pages_count(html) + 1):
        pass
