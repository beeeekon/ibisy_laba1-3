package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

//реализовать алгоритм шифрования RSA
//даны два целых числа p и q
//алгоритм:
//1) считаем n=p*q
//2) считаем phi=(p-1)(q-1)
//3) ищем число e, такое что e<phi и e c phi- взаимно простые числа (их наибольший общий делитель 1)
//4) вычисляем d такое чтобы (d*e)%phi=1 и d<phi
//открытый ключ пара {e,n}
//закрытый ключ пара {d,n}
public class RSACipher implements TypeCipher {
    private class Pair {
        private int firstVar;
        private int secondVar;

        public Pair(int a, int b) {
            firstVar = a;
            secondVar = b;
        }

        protected void setFirstVar(int a) {
            firstVar = a;
        }

        protected void setSecondVar(int b) {
            secondVar = b;
        }

        public int getFirstVar() {
            return firstVar;
        }

        public int getSecondVar() {
            return secondVar;
        }

        @Override
        public String toString() {
            return "(" + Integer.toString(firstVar) + "," + Integer.toString(secondVar) + ")";
        }
    }

    private Pair sources;//в зависимости от первой вызванной функции будет определяться, что именно это
    private int e = -1000;
    private int d = -1000;
    private int n = -1000;
    private int orientation = -1;//1- шифрование, 0 - дешифрование, -1-направление не было задано
    private String shifr="abcdefghijklmnopqrstuvwxyz";


    public RSACipher(int a, int b) {
        if (a > 1 && b > 1) //тк потом будет (p-1)(q-1) должно быть простым положительным числом
            sources = new Pair(a, b);
        else
            throw new IllegalArgumentException();//недопустимый аргумент
    }

    //суть в том что при первом использовании классу присвоится стиль работы
    //то есть если изначально первым методом была вызвана шифровка, то числа sources станут p и q
    //и в дальнейшем поля e,d,n заполнятся полностью.
    //при следующих запусках программы при попытке использовать дешифрование затребуется пароль (d,n)
    //если первым было использовано дешифрование, то сразу заполнятся только (d,n), а e-останется -1000
    //при следующем запуске программы и вызывании шифровки будет предложено ввести значения (p,q), и все перезапишется
    private boolean checkUsage(boolean orientation) throws IOException, InterruptedException {//true- шифр, false - дешифр
        boolean result = false;
        if (this.orientation == -1) {//присваиваем направление первый раз
            if (orientation)
                this.orientation = 1;
            else {
                this.orientation = 0;
                this.d = sources.getFirstVar();
                this.n = sources.getSecondVar();
            }
            result = true;
        } else {
            if (this.orientation == 1 && !orientation) {//было на шифровку, а просят дешифровку
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int x;
                int y;
                System.out.println("Please, enter private key: d=");
                x = Integer.parseInt(reader.readLine());
                System.out.print("  n=");
                y = Integer.parseInt(reader.readLine());
                if (x != this.d || y != this.n) {
                    System.out.println("Your entered key is not being verified");
                    result = false;
                } else
                    result = true;
            } else if (this.orientation == 0 && orientation) {//было на дешифр, а просят шифровку
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Your request cannot being fulfilled. Do you want lose data and enter new open key?");
                System.out.println("1 - Yes");
                System.out.println("2 - No");
                int x;
                x = Integer.parseInt(reader.readLine());//ввод с консоли
                while (x != 1 && x != 2) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();//очистка консоли
                    System.out.println("Your request cannot being fulfilled. Do you want lose data and enter new open key?");
                    System.out.println();
                    System.out.println("PLEASE ENTER AN OPTION FROM THE OPTIONS BELOW");
                    System.out.println("1 - Yes");
                    System.out.println("2 - No");
                    x = Integer.parseInt(reader.readLine());
                }
                if (x == 1) {
                    System.out.println("Please, enter public key: p=");
                    x = Integer.parseInt(reader.readLine());
                    sources.setFirstVar(x);
                    System.out.print("  q=");
                    x = Integer.parseInt(reader.readLine());
                    sources.setSecondVar(x);
                    //скидываем все до начальных настроек и перенаправляем действие класса
                    this.e = -1000;
                    this.d = -1000;
                    this.n = -1000;
                    this.orientation = 1;
                    result = true;
                } else
                    result = false;
            } else
                result = true;
        }
        return result;
    }

    private int nod(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private void calculateSources() {//функция вызывается только из encription!!!! и только для первоначального рассчета
        if (this.orientation != 1 || this.e != -1000 || this.d != -1000 || this.n != -1000) {
            //иначе у нас не шифрование или не первоначальный вызов
            throw new IllegalArgumentException();//недопустимый аргумент
        }
        int p = sources.getFirstVar();
        int q = sources.getSecondVar();
        int n = p * q;
        int phi = (p - 1) * (q - 1);
        //нам надо найти положительное простое e
        //пусть оно не больше 50
        int e = phi;
        boolean isVerniyNod = nod(e, phi) == 1;
        while ((e < phi && isVerniyNod) == false && e > 1) {
            e--;
            isVerniyNod = nod(e, phi) == 1;
        }

        if (e == 50) {
            if ((e < phi && nod(e, phi) == 1) == false)//значит мы дошли до 50 и е так и не подошло
                throw new IllegalArgumentException();//недопустимый аргумент
        }
        this.e = e;
        this.n = n;
        //теперь еще посчитаем d тоже не больше 50
        int d = 1;
        while (((d * e) % phi == 1 && d < phi) == false && d <= 50)
            d++;
        if (d == 50)
            if (((d * e) % phi == 1 && d < phi) == false)//значит мы перебрали все d и даже на 50 число не подошло
                throw new IllegalArgumentException();//недопустимый аргумент
        this.d = d;
    }

    private boolean isSeparatorHere(String word){
        boolean flag=false;
        for(int i=0;i<word.length()&!flag;i++){
            flag=word.charAt(i)==';';
        }
        return flag;
    }
    private String editWordWithSeparator(String word){
        String exit="";

        if (isSeparatorHere(word)) {//если сепаратор есть, то его надо убрать
            String[] arr=word.split(";");
            exit= Arrays.toString(arr);
        }
        else{
            for(int i=0;i<word.length();i++)
                exit+=word.charAt(i)+";";

        }
        return exit;
    }
    private String convertToKey(String word){
        //word=word.toLowerCase();//все сгоняем в нижний регистр
        String exit="";
        String[] ww=word.split(";");
        //если передать в метод indexOf букву, то он вернет индект вхождения
        for (int i=0;i<ww.length;i++){
            exit+=shifr.indexOf(ww[i]);
            exit+=";";
        }
        exit=exit.substring(0, exit.length()-1);//удалили последнюю ;
        return exit;
    }

    private boolean isEnglishAllLetters(String word){
        word=word.toLowerCase();
        boolean flag=true;
        for(int i=0;i<word.length()&&flag;i++) {
            boolean x=word.charAt(i)>='a'&&word.charAt(i)<='z';
            flag = flag &&x;
        }
        return flag;
    }

    public String encryption(String word) throws IOException, InterruptedException {
        if (word == "")
            return "";
        String result = "";
        if (checkUsage(true)) {
            calculateSources();//рассчитываем там все данные если нужно
            if(!isEnglishAllLetters(word)) {
                System.out.println("Sorry, your request was rejected");
                return result;
            }
            word=editWordWithSeparator(word);
            word=convertToKey(word);//делаем из слова строку с индексами по типу "1;2;3;4"
            String[] ww=word.split(";");//делаем массив стринг из [1,2,3,4]
            for (int i = 0; i < ww.length; i++) {
                int a = ((Integer.parseInt(ww[i])+1 )^ this.e) % this.n;
                result += a+";";
            }
            //на данный момент зашифрованное слово в виде типа "2;5;12;21;" и тп
            result=result.substring(0,result.length()-1);//"2;5;12;21"
            //надо перегнать числа в символьный вид:
            String charkey="";
            String[] wor=result.split(";");
            for (int i=0;i<wor.length;i++){
                charkey+=Character.toString((char)Integer.parseInt(wor[i]));
                charkey+=";";
            }
            charkey=charkey.substring(0, charkey.length()-1);//удалили последнюю ;
            result=charkey;//теперь это "a;b;c;d"
            String[] exit=result.split(";");//[a,b,c,d]
            result=String.join("",exit);//"abcd" ура получили что надо
        } else {
            System.out.println("Sorry, your request was rejected");
        }
        return result;
    }

    public String decryption(String word) throws IOException, InterruptedException {
        if (word == "")
            return "";
        String result = "";
        if (checkUsage(false)) {
            word=editWordWithSeparator(word);//[a;b;c;d]
            //word=convertToKey(word);//делаем из слова строку с индексами по типу "1;2;3;4"
            String[] ww=word.split(";");//делаем массив стринг из [1,2,3,4]
            for (int i = 0; i < ww.length; i++) {
                String s=ww[i];
                char x=s.charAt(0);
                int y=x^this.d;
                int a = (x^ this.d) % this.n-1;//////////////////////////////
                result += a%26+";";
            }

            result=result.substring(0,result.length()-1);//"2;5;12;21"
            //result=convertToKey(result);

            String keytochar="";
            String[] wor=result.split(";");

            for (int i=0;i<wor.length;i++){

                keytochar+=shifr.charAt(Integer.parseInt(wor[i]));
                keytochar+=";";
            }
            keytochar=keytochar.substring(0, keytochar.length()-1);//удалили последнюю ;
            result=keytochar;
            String[] exit=result.split(";");//[a,b,c,d]
            result=String.join("",exit);//"abcd" ура получили что надо
        } else {
            System.out.println("Sorry, your request was rejected");
        }


        return result;
    }
}
