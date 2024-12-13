package org.example;

//организовать алгоритм фишрования заменой Виженера
//есть ключ по которому будем расшифровывать и фича сдвиг таблицы Виженера
//дано слово для кодирования. Ключ делаем в длину слова.
//ключ по строкам, слово по столбцам. На пересечении будет буква для шифра

import java.io.IOException;

public class ReplaceVizheneraCipher implements TypeCipher{
    private String key;//ключ обязательно в нижнем регистре
    //private int shift;//сдвиг строк на колво символов
    private int kolvoLetters;//количество букв в алфавите
    private boolean alphabet;// true-русский, false -английский
    private String shifr;//строка алфавита


    // true-русский, false -английский
    private boolean whatAlphabet(String word){
        //-2 изначальное положение
        //-1 то что мы нашли все было английское
        //0 мы нашли и английское и русское
        //1 то что мы нашли все русское
        int flag=-2;
        for(int i=0; i<word.length() && flag!=0; i++){
            if(word.charAt(i)>='a'&&word.charAt(i)<='z'){
                if(flag!=-1&&flag!=-2)//значит были до этого русские буквы
                    flag=0;
                else flag=-1;
            }
            else if (word.charAt(i)>='а'&&word.charAt(i)<='я') {
                if(flag!=1&&flag!=-2)//значит были до этого английские буквы
                    flag=0;
                else flag=1;
            }
            else//значит в слове не понятные значки или буквы другого алфавита
                flag=0;

        }
        boolean exit;//какой в итоге то язык
        switch (flag){
            case -2://значит мы в фор и не зашли, значит слово - пустая строка
            case 0:{//значит либо там буквы с разных алфавитов, либо вообще другие символы
                throw new IllegalArgumentException();//недопустимый аргумент
            }
            case -1:{//значит все буквы были английскими
                exit=false;
                break;
            }

            case 1:{//все русские буквы
                exit=true;
                break;
            }
            default:{//лажа в проге, вообще не должны сюда прийти
                System.out.println("ReplaceVizheneraCipher:: private boolean whatAlphabet   -  что-то пошло не так со словом "+word);
                throw new IllegalArgumentException();//недопустимый аргумент
            }
        }
        return exit;
    }

    public ReplaceVizheneraCipher(String k){
        k=k.toLowerCase();
        boolean alph =whatAlphabet(k);
        //если функция выше не вызовет exception, то нам слово априори подходит
        key=k;
        alphabet=alph;
        if(alph) {//русский
            kolvoLetters = 33;
            shifr="абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        }
        else {//английский
            kolvoLetters = 26;
            shifr="abcdefghijklmnopqrstuvwxyz";
        }
    }

    //метод возвращает ключ длины, равной слову
    private String matchKeyAndWord(String word){
        String exit="";//новый ключ который будет
        for(int i=0;i<word.length();i++)
            exit+=key.charAt(i%key.length());
        return exit;
    }

    //метод по шаблонному паттерну: выполняет кодирование в обе стороны
    //true- шифровка, false - расшифровка
    private String commonCoded(String word,boolean orientation){
        word=word.toLowerCase();
        if(alphabet!=whatAlphabet(word)) {//если слова из разных алфавитов, то ниче не можем сделать
            System.out.println("ReplaceVizheneraCipher:: public String encryption   -  ключ и слово из разных алфавитов");
            throw new IllegalArgumentException();//недопустимый аргумент
        }
        String exit="";//зашифрованное или расшифрованное слово
        String newkey=matchKeyAndWord(word);
        for(int i=0;i<word.length();i++){
            String currentStrMatrix= shifr.substring(0,newkey.charAt(i)-shifr.charAt(0));
            currentStrMatrix=shifr.substring(newkey.charAt(i)-shifr.charAt(0))+currentStrMatrix;
            if(orientation) {
                int x=(word.charAt(i)-shifr.charAt(0)) % kolvoLetters;
                exit += currentStrMatrix.charAt(x);
            }
            else {
                int x=(word.charAt(i) + kolvoLetters - newkey.charAt(i)) % kolvoLetters;
                exit += shifr.charAt(x);
            }
        }
        return exit;
    }

    public String encryption(String word)throws IOException, InterruptedException{
        boolean orientation = true;
        String resultWord=commonCoded(word,orientation);
        return resultWord;
    }
    public String decryption(String word)throws IOException, InterruptedException{
        boolean orientation = false;
        String resultWord=commonCoded(word,orientation);
        return resultWord;
    }

}
