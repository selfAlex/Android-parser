package my.functions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Parser {

    static String url_cpu_shop = "";
    static String url_cpu_technodom = "";
    static String url_cpu_satu = "";

    static String url_graphiccards_shop = "";
    static String url_graphiccards_technodom = "";
    static String url_graphiccards_satu = "";

    static String url_motherboards_shop = "";
    static String url_motherboards_technodom = "";
    static String url_motherboards_satu = "";

    public void define_urls(String hardware_type) {

        switch (hardware_type) {
            case "CPU Processors":

                url_cpu_shop = "https://shop.kz/protsessory/filter/karaganda-is-v_nalichii-or-ojidaem-or-dostavim/apply/";
                url_cpu_technodom = "https://www.technodom.kz/karaganda/noutbuki-i-komp-jutery/komplektujuschie/processory";
                url_cpu_satu = "https://satu.kz/search?category=70701&search_term=процессоры&a5527=135299";

                break;

            case "Graphic cards":

                url_graphiccards_shop = "https://shop.kz/videokarty/filter/karaganda-is-v_nalichii-or-ojidaem-or-dostavim/apply/";
                url_graphiccards_technodom = "https://www.technodom.kz/karaganda/noutbuki-i-komp-jutery/komplektujuschie/videokarty";
                url_graphiccards_satu = "https://satu.kz/Videokarty?a5527=135299";

                break;

            case "Motherboards":

                url_motherboards_shop = "https://shop.kz/materinskie-platy/filter/karaganda-is-v_nalichii-or-ojidaem-or-dostavim/apply/";
                url_motherboards_technodom = "https://www.technodom.kz/karaganda/noutbuki-i-komp-jutery/komplektujuschie/materinskie-platy";
                url_motherboards_satu = "https://satu.kz/Materinskie-platy?a5527=135299";

                break;
        }

    }

    public static void main(String[] args) {
        System.out.println(url_graphiccards_shop);
    }

//    public void parse_graphic_cards() {
//
//        Document doc = Jsoup.connect('').get()
//
//    }

}
