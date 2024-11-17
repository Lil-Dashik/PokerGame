package PokerGame;

import java.util.List;

public class PokerGame {
    public static void main(String[] args) {
        DealerDeal dealer = new DealerDeal(new Deck());
        Board board = dealer.dealCardsToPlayers();


        // Раздача флопа
        Board board1 = dealer.dealFlop(board);


        // Раздача терна
        Board board2 = dealer.dealTurn(board1);


        // Раздача ривера
        Board board3 = dealer.dealRiver(board2);
        System.out.println(board3);

        // Получаем карты игроков
        List<Card> playerOneCards = dealer.parseCards(board.getPlayerOne());
        List<Card> playerTwoCards = dealer.parseCards(board.getPlayerTwo());

        // Получаем общие карты
        List<Card> communityCards = dealer.parseCards(board3.getFlop());
        if (board3.getTurn() != null) communityCards.addAll(dealer.parseCards(board3.getTurn()));
        if (board3.getRiver() != null) communityCards.addAll(dealer.parseCards(board3.getRiver()));


        // Оцениваем комбинации для обоих игроков
        HandPlayer playerOneBestHand = PokerHandEvaluator.evaluateBestHand(playerOneCards, communityCards);
        HandPlayer playerTwoBestHand = PokerHandEvaluator.evaluateBestHand(playerTwoCards, communityCards);
        System.out.println("Player One Best Combination: " + playerOneBestHand.getPriority() + " with cards: " + playerOneBestHand.getCards());
        System.out.println("Player Two Best Combination: " + playerTwoBestHand.getPriority() + " with cards: " + playerTwoBestHand.getCards());


        // Определяем победителя
        PokerResult result = dealer.decideWinner(board3);

        // Вывод результата
        switch (result) {
            case PLAYER_ONE_WIN:
                System.out.println("Final Result: PLAYER_ONE_WIN");
                break;
            case PLAYER_TWO_WIN:
                System.out.println("Final Result: PLAYER_TWO_WIN");
                break;
            case DRAW:
                System.out.println("Final Result: DRAW");
                break;
        }
        PokerValidator.validateBoard(board3);
    }
}

