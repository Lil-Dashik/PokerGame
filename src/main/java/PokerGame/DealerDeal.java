package PokerGame;

import java.util.*;
import java.util.stream.Collectors;

public class DealerDeal implements Dealer {
    private final Deck deck;

    public DealerDeal() {
        this.deck = new Deck();
    }

    @Override
    public Board dealCardsToPlayers() {
        if (deck.size() < 4) {
            throw new IllegalStateException("Not enough cards to deal to players.");
        }
        String playerOne = deck.dealCard().toString() + deck.dealCard().toString();
        String playerTwo = deck.dealCard().toString() + deck.dealCard().toString();
        Board newBoard = new Board(playerOne, playerTwo);
        return newBoard;
    }

    @Override
    public Board dealFlop(Board board) {
        PokerValidator.validateBoard(board,Stage.PLAYERS_CARDS);
        if (deck.size() < 3) {
            throw new IllegalStateException("Not enough cards to deal the flop.");
        }
        String flop = deck.dealCard().toString() + deck.dealCard().toString() + deck.dealCard().toString();
        Board newBoard = new Board(board.getPlayerOne(), board.getPlayerTwo(), flop);
        return newBoard;
    }

    @Override
    public Board dealTurn(Board board) {
        PokerValidator.validateBoard(board, Stage.FLOP);
        if (deck.size() < 1) {
            throw new IllegalStateException("Not enough cards to deal the turn.");
        }
        String turn = deck.dealCard().toString();
        Board newBoard = new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), turn);
        return newBoard;
    }

    @Override
    public Board dealRiver(Board board) {
        PokerValidator.validateBoard(board,Stage.TURN);
        if (deck.size() < 1) {
            throw new IllegalStateException("Not enough cards to deal the river.");
        }
        String river = deck.dealCard().toString();
        Board newBoard = new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), board.getTurn(), river);
        return newBoard;
    }


    @Override
    public PokerResult decideWinner(Board board) throws InvalidPokerBoardException {
        PokerValidator.validateBoard(board, Stage.RIVER);

        List<Card> playerOneCards = Card.parseCards(board.getPlayerOne());
        List<Card> playerTwoCards = Card.parseCards(board.getPlayerTwo());
        List<Card> communityCards = getCommunityCards(board);

        HandPlayer playerOneBestHand = PokerHandEvaluator.evaluateBestHand(playerOneCards, communityCards);
        HandPlayer playerTwoBestHand = PokerHandEvaluator.evaluateBestHand(playerTwoCards, communityCards);

        System.out.println("Player One Best Combination: " + playerOneBestHand.getPriority() + " with cards: " + playerOneBestHand.getCards());
        System.out.println("Player Two Best Combination: " + playerTwoBestHand.getPriority() + " with cards: " + playerTwoBestHand.getCards());
        PokerResult result = determineWinner(playerOneBestHand, playerTwoBestHand);
        printResult(result);
        return result;
    }

    public PokerResult determineWinner(HandPlayer playerOneHand, HandPlayer playerTwoHand) {
        int comparison = HandPlayer.compareHands(playerOneHand, playerTwoHand);

        if (comparison > 0) {
            return PokerResult.PLAYER_ONE_WIN;
        } else if (comparison < 0) {
            return PokerResult.PLAYER_TWO_WIN;
        } else {
            return PokerResult.DRAW;
        }
    }

//
//        if (sortedHand1.equals(sortedHand2)) {
//            return 0; // Ничья
//        }
//        // Убираем карты комбинации из общего списка для кикеров
//        Set<Integer> combinationRanksPlayer1 = hand1.getCards().stream()
//                .map(Card::getRankValue)
//                .collect(Collectors.toSet());
//        Set<Integer> combinationRanksPlayer2 = hand2.getCards().stream()
//                .map(Card::getRankValue)
//                .collect(Collectors.toSet());
//        System.out.println("combinationRanksPlayer1:"+combinationRanksPlayer1 + " " + "combinationRanksPlayer2:" +combinationRanksPlayer2);
//        // Если комбинации равны, учитываем кикеры
//        List<Card> kicker1 = sortedHand1.stream()
//                .filter(card -> !combinationRanksPlayer1.contains(card.getRankValue()))
//                .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
//                .toList();
//        List<Card> kicker2 = sortedHand2.stream()
//                .filter(card -> !combinationRanksPlayer2.contains(card.getRankValue()))
//                .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
//                .toList();
//        System.out.println("Kickers for Player 1: " + kicker1);
//        System.out.println("Kickers for Player 2: " + kicker2);
//        for (int i = 0; i < Math.min(kicker1.size(), kicker2.size()); i++) {
//            int kickerComparison = Integer.compare(kicker1.get(i).getRankValue(), kicker2.get(i).getRankValue());
//            if (kickerComparison != 0) {
//                return kickerComparison;
//            }
//        }
//


    private void printResult(PokerResult result) {

        switch (result) {
            case PLAYER_ONE_WIN -> System.out.println("Final Result: PLAYER_ONE_WIN");
            case PLAYER_TWO_WIN -> System.out.println("Final Result: PLAYER_TWO_WIN");
            case DRAW -> System.out.println("Final Result: DRAW");
        }

    }

    private List<Card> getCommunityCards(Board board) {
        List<Card> communityCards = Card.parseCards(board.getFlop());
        communityCards.addAll(Card.parseCards(board.getTurn()));
        communityCards.addAll(Card.parseCards(board.getRiver()));
        return communityCards;
    }

}

