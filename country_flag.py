# -*- coding: utf-8 -*-

import urllib.request
import requests
from bs4 import BeautifulSoup

def download_flag(country):

    # 请求头部
    headers = {
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36',
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
              }
    # POST数据
    data = {'q': country}
    # 网址
    url = 'http://country.911cha.com/'

    # 提交POST请求
    r = requests.post(url=url, data=data, headers=headers)

    content = BeautifulSoup(r.text, 'lxml')

    country = content.find_all('div', class_='mcon')[1]('ul')[0]('li')[0]('a')[0]
    link = country['href']

    r2 = requests.get(url='%s/%s'%(url, link))

    content = BeautifulSoup(r2.text, 'lxml')
    images = content.find_all('img')

    for image in images:
        if 'alt' in image.attrs:
            if '国旗' in image['alt']:
                name = image['alt'].replace('国旗', '')
                link = image['src']

    # 下载图片
    urllib.request.urlretrieve('%s/%s'%(url, link), 'E://flag/%s.gif'%name)


def main():

    file = 'E://flag/countries.txt'
    with open(file, 'r') as f:
        counties = [_.strip() for _ in f.readlines()]


    for country in counties:
        try:
            download_flag(country)
            print('%s国旗下载成功！'%country)
        except:
            print('%s国旗下载失败~'%country)

main()