package com.example.geoskills2.model;

public class Question {

   private String statement;
   private String answer;
   private String userAnswer;

   private String[] choices;



   public Question(){

   }

   public Question( String statement, String answer, String[] choices, String userAnswer) {

      this.statement = statement;
      this.answer = answer;
      this.choices = choices;
      this.userAnswer = userAnswer;
   }



   public String getStatement() {
      return statement;
   }

   public String getAnswer() {
      return answer;
   }

   public String[] getChoices() {
      return choices;
   }

   public String getUserAnswer() {
      return userAnswer;
   }

   public void setUserAnswer(String userAnswer) {
      this.userAnswer = userAnswer;
   }
}
