package org.example;

import java.io.IOException;
import java.util.*;

//реализовать алгоритм двойной перестановки для шифрования
//должно быть два слова для расшифровки, одно по строкам, другое по столбцам
public class DoublePermutationCipher implements TypeCipher{

    private String keyFirst;//ключевое слово для строк
    private String keySecond;//ключевое слово для столбцов

    public DoublePermutationCipher(String wordFirst, String wordSecond){
        if(wordFirst.length()==0||wordSecond.length()==0)
            throw new IllegalArgumentException();//недопустимый аргумент
        keyFirst=wordFirst;
        keySecond=wordSecond;
    }
    private String removeDuplicateLetters(String word){//в ключах не может быть повторяющихся букв
        HashSet<Character> boxLetters = new HashSet<Character>();
        String finalStroka="";
        for(int i=0;i<word.length();i++){
            if(boxLetters.add(word.charAt(i))){//если такой символ еще не использовали, то записали в сет
                finalStroka+=word.charAt(i);//и добавили в финальный ключ
            }
        }
        return finalStroka;
    }

    private String massivPermutation(String word){//будет преобразовывать слово в числовой ключ
        word= removeDuplicateLetters(word);//убираем повторяющиеся буквы или цифры
        String finalNumberKey="";

        boolean allLettersIsNumbers=true;
        /*for(int i=0;i<word.length();i++) {//если слово состоит только из чисел, то его надо оставить в первозданном виде

            boolean isLetterNumber=word.charAt(i) >= '0' && word.charAt(i) <= '9';//число ли?
            if (isLetterNumber)
                allLettersIsNumbers = allLettersIsNumbers && isLetterNumber;
        }
        if(allLettersIsNumbers)//если все числа, то оставляем так
            finalNumberKey=word;
        else{
        */
        //если нет то сортируем по аски

        HashMap<Character, Integer> numberKey = new HashMap<>();
        char[] wordLetters = word.toCharArray();//завели массив букв
        Arrays.sort(wordLetters);//отсортировали
        for(int i=0;i<wordLetters.length;i++)
            numberKey.put(wordLetters[i],i+1);
        //каждой букве поставили в соответствие порядковый номер по отсортировынному списку

        for(int i=0;i<word.length();i++)
            finalNumberKey+=numberKey.get(word.charAt(i));
        //}

        return finalNumberKey;
    }
    private ArrayList<ArrayList<Character>> createOrigMassiv (String word){//закидывает исходную фразу в массив
        keyFirst=massivPermutation(keyFirst);
        keySecond=massivPermutation(keySecond);
        //сделали ключи такими, какими надо
        if(!sizeMatching(word))//если у нас длины слова не хватит на заполнение массива то выбрасываем исключение
            throw new IllegalArgumentException();
        ArrayList<ArrayList<Character>> masOrig = new ArrayList<ArrayList<Character>>();
        System.out.println("original massiv:");
        for (int i=0;i<keyFirst.length();i++){
            ArrayList<Character> masOrigLine= new ArrayList<Character>();
            for(int j=0;j<keySecond.length();j++) {
                Character letter=word.charAt(i * keySecond.length() + j);
                masOrigLine.add(letter);
                System.out.print(letter+" ");
            }
            System.out.println();
            masOrig.add(masOrigLine);
        }
        System.out.println();
        return masOrig;
    }

    private  HashMap<Integer, Integer> createPermutation(String newKey, boolean orientation){
        HashMap<Integer, Integer> permutation= new HashMap<>();
        for(int i=0;i<newKey.length();i++) {
            if (orientation)//для кодирования
                permutation.put(i, newKey.charAt(i) - '0'-1);
            else//для раскодирования
                permutation.put(newKey.charAt(i) - '0'-1, i);
            //такой загон связан с тем, что я не нашла метода у hashmap как вытянуть ключ по объекту
        }
        return permutation;
    }


    private boolean sizeMatching(String word){//проверка для упрощенного варианта. Точно ли длины слова хватит
        return keyFirst.length()*keySecond.length()==word.length();
    }

    //метод по шаблонному паттерну: выполняет кодирование в обе стороны
    //true- шифровка, false - расшифровка
    private String commonCoded(String word,boolean orientation){
        //записали фразу в массив
        ArrayList<ArrayList<Character>> masOrig = createOrigMassiv(word);
        //поставим соответсвие порядковых номеров ключам
        HashMap<Integer, Integer> permutationFirst= createPermutation(keyFirst, orientation);//соответствие по строкам keyFirst
        HashMap<Integer, Integer> permutationSecond= createPermutation(keySecond, orientation);//соответствие по строкам keySecond
        ArrayList<ArrayList<Character>> masEncription = new ArrayList<ArrayList<Character>>();
        String resultWord="";
        System.out.println("ciphering massiv:");
        for (int i=0;i<keyFirst.length();i++){//создаем новый массив
            ArrayList<Character> masEncrLine= new ArrayList<Character>();
            for(int j=0;j<keySecond.length();j++) {
                Character letter=masOrig.get(permutationFirst.get(i)).get(permutationSecond.get(j));
                masEncrLine.add(letter);
                resultWord+=letter;
                System.out.print(letter+" ");
            }
            System.out.println();
            masEncription.add(masEncrLine);
        }
        System.out.println();
        System.out.println("result ciphering string:");
        System.out.println(resultWord);
        return resultWord;

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
