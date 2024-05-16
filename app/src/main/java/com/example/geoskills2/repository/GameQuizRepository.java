package com.example.geoskills2.repository;

import com.example.geoskills2.model.Question;

import java.util.ArrayList;
import java.util.List;

public class GameQuizRepository {
    public static List<Question> getListQuestions(){
        ArrayList<Question> geographyQuestions = new ArrayList<>();

        // Pergunta 1
        String statement1 = "Qual é a capital do Brasil?";
        String answer1 = "Brasília";
        String[] choices1 = {"Rio de Janeiro", "São Paulo", "Brasília", "Salvador"};
        String userAnswer1 = "";
        Question question1 = new Question(statement1, answer1, choices1, userAnswer1);
        geographyQuestions.add(question1);

        // Pergunta 2
        String statement2 = "Qual é o maior oceano do mundo?";
        String answer2 = "Oceano Pacífico";
        String[] choices2 = {"Oceano Atlântico", "Oceano Índico", "Oceano Pacífico", "Oceano Antártico"};
        String userAnswer2 = "";
        Question question2 = new Question(statement2, answer2, choices2, userAnswer2);
        geographyQuestions.add(question2);

        // Pergunta 3
        String statement3 = "Qual é o ponto mais alto do mundo?";
        String answer3 = "Monte Everest";
        String[] choices3 = {"Monte Kilimanjaro", "Monte Aconcágua", "Monte Everest", "Monte McKinley"};
        String userAnswer3 = "";
        Question question3 = new Question(statement3, answer3, choices3, userAnswer3);
        geographyQuestions.add(question3);

        // Pergunta 4
        String statement4 = "Qual é o maior rio do mundo em volume de água?";
        String answer4 = "Rio Amazonas";
        String[] choices4 = {"Rio Nilo", "Rio Amazonas", "Rio Yangtzé", "Rio Mississipi"};
        String userAnswer4 = "";
        Question question4 = new Question(statement4, answer4, choices4, userAnswer4);
        geographyQuestions.add(question4);

        // Pergunta 5
        String statement5 = "Qual é o maior deserto do mundo?";
        String answer5 = "Deserto do Saara";
        String[] choices5 = {"Deserto de Atacama", "Deserto do Saara", "Deserto da Arábia", "Deserto da Patagônia"};
        String userAnswer5 = "";
        Question question5 = new Question(statement5, answer5, choices5, userAnswer5);
        geographyQuestions.add(question5);

        return geographyQuestions;
    }
}
