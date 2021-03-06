package com.example.covid_19_update;

public class Hotlines {

//    public Hotlines(String[] states, String[][] numbers) {
//        this.states = states;
//        this.numbers = numbers;
//    }

    String [] states = {
            "Benue State", "Federal Capital Territory", "Kogi State", "Kwara State", "Niger State", "Plateau State",
    "Kaduna State", "Kano State", "Katsina State", "Sokoto State", "Zamfara State", "Adamawa State", "Bauchi State",
    "Borno State", "Gombe State", "Taraba State", "Yobe State", "Akwa Ibom State", "Bayelsa State", "Cross River State",
    "Delta State", "Edo State", "Abia State", "Anambra State", "Enugu State", "Ebonyi State", "Imo State", "Ogun State",
    "Ondo State", "Osun State", "Oyo state", "Ekiti State", "Lagos State"};

    String [][] numbers = {
            {"09018602439", "07025031214"},
            {"08099936312", "07080631500"},
            {"0704485619", "07088292249", "08150953486", "08095227003", "07043402122"},
            {"090620100001", "09062010002"}, {"08038246018", "09093093642", "08077213070"}, {"07032864444", "08035422711", "08065486416", "08035779917"},
            {"08035871662", "08025088304", "08032401473", "08037808191"}, {"08039704476", "08037038597", "09093995333", "09093995444"},
            {"09035037114", "09047092428"}, {"08032311116", "08022069567", "08035074228", "07031935037", "08036394462"},
            {"08035626731", "08035161538", "08161330774", "08065408696", "08105009888"}, {"08031230359", "07080601139"},
            {"08032717887", "08059600898", "08080330216", "08036911698"}, {"08088159881", "080099999999"}, {"08103371257"},
            {"08032501165", "08065508675", "08039359368", "08037450227"}, {"08131834764", "07041116027"}, {"09045575515", "07035211919", "03028442194", "08037934966", "09023330092"},
            {"08039216821", "07019304970", "08151693570"}, {"09036281412", "08050907736"}, {"08033521961", "08035078541" , "08030758179", "08031230021"},
            {"08084096723", "08064258163", "08035835529"}, {"07002242362"}, {"08030953771", "08117567363", "08145434416"},
            {"08182555550", "117", "112"}, {"09020332489"}, {"08099555577", "07087110839"}, {"08188978392", "09188978392"},
            {"08002684319", "07002684319", "070012684319"}, {"08035025692", "08033908772", "08056456250"}, {"08038210122", "08023229267", "08073431342"},
            {"09062970434", "09062970435", "09062970436", "112"}, {"08023169485", "08033565529", "08052817243", "08028971864"}};

    String [] questionsArr = {"Are you dry coughing?", "Do you have a flu ?", "Is your throat sore?", "Are You having diarrhea?", "Are you experiencing body/joint pains?",
                                "Do you have headache?", "Do you have a fever?", "Are you having difficult breathing?", "Are you experiencing fatigue?", "Have you travelled recently?",
        "Have you travelled to coronavirus infected area?", "Do you have contact or taking care of covid-19 positive patient?"};

    String [] pointsArr = {"2", "1", "1", "1", "1", "1", "1", "3", "1", "1", "2", "3" };

    public String[] getStates() {
        return states;
    }

    public String[][] getNumbers() {
        return numbers;
    }

    public String[] getQuestionsArr () {return questionsArr; }

    public String[] getPointsArr() {
        return pointsArr;
    }

}
