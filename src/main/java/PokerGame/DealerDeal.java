package PokerGame;

import java.util.ArrayList;
import java.util.List;

public class DealerDeal implements Dealer {
    private final Deck deck;

    public DealerDeal(Deck deck) {
        this.deck = deck;
    }

    @Override
    public Board dealCardsToPlayers() {
        if (deck.size() < 4) {
            throw new IllegalStateException("Not enough cards to deal to players.");
        }
        String playerOne = deck.dealCard().toString() + deck.dealCard().toString();
        String playerTwo = deck.dealCard().toString() + deck.dealCard().toString();
        Board newBoard = new Board(playerOne, playerTwo);
        PokerValidator.validateBoard(newBoard); // Проверка корректности
        return newBoard;
    }

    @Override
    public Board dealFlop(Board board) {
        if (deck.size() < 3) {
            throw new IllegalStateException("Not enough cards to deal the flop.");
        }
        String flop = deck.dealCard().toString() + deck.dealCard().toString() + deck.dealCard().toString();
        Board newBoard = new Board(board.getPlayerOne(), board.getPlayerTwo(), flop);
        PokerValidator.validateBoard(newBoard); // Проверка корректности
        return newBoard;
    }

    @Override
    public Board dealTurn(Board board) {
        if (deck.size() < 1) {
            throw new IllegalStateException("Not enough cards to deal the turn.");
        }
        String turn = deck.dealCard().toString();
        Board newBoard = new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), turn);
        PokerValidator.validateBoard(newBoard); // Проверка корректности
        return newBoard;
    }

    @Override
    public Board dealRiver(Board board) {
        if (deck.size() < 1) {
            throw new IllegalStateException("Not enough cards to deal the river.");
        }
        String river = deck.dealCard().toString();
        Board newBoard = new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), board.getTurn(), river);
        PokerValidator.validateBoard(newBoard); // Проверка корректности
        return newBoard;
    }


    @Override
    public PokerResult decideWinner(Board board) throws InvalidPokerBoardException {
        PokerValidator.printValidationMessageIfNeeded();
        PokerValidator.validateBoard(board);

        List<Card> playerOneCards = parseCards(board.getPlayerOne());
        List<Card> playerTwoCards = parseCards(board.getPlayerTwo());
        List<Card> communityCards = getCommunityCards(board);


        // Находим лучшую комбинацию для каждого игрока
        HandPlayer playerOneBestHand = PokerHandEvaluator.evaluateBestHand(playerOneCards, communityCards);
        HandPlayer playerTwoBestHand = PokerHandEvaluator.evaluateBestHand(playerTwoCards, communityCards);

        printBestHands(playerOneBestHand, playerTwoBestHand);
        PokerResult result = determineWinner(playerOneBestHand, playerTwoBestHand);
        printResult(result);
        PokerValidator.validateBoard(board);
        return result;
    }

    private PokerResult determineWinner(HandPlayer playerOneBestHand, HandPlayer playerTwoBestHand) {
        int comparisonResult = playerOneBestHand.compareTo(playerTwoBestHand);

        if (comparisonResult > 0) {
            return PokerResult.PLAYER_ONE_WIN;
        } else if (comparisonResult < 0) {
            return PokerResult.PLAYER_TWO_WIN;
        } else {
            return PokerResult.DRAW;
        }
    }

    private void printResult(PokerResult result) {

        switch (result) {
            case PLAYER_ONE_WIN -> System.out.println("Final Result: PLAYER_ONE_WIN");
            case PLAYER_TWO_WIN -> System.out.println("Final Result: PLAYER_TWO_WIN");
            case DRAW -> System.out.println("Final Result: DRAW");
        }

    }

    private List<Card> getCommunityCards(Board board) {
        List<Card> communityCards = parseCards(board.getFlop());
        if (board.getTurn() != null) communityCards.addAll(parseCards(board.getTurn()));
        if (board.getRiver() != null) communityCards.addAll(parseCards(board.getRiver()));
        return communityCards;
    }

    private void printBestHands(HandPlayer playerOneBestHand, HandPlayer playerTwoBestHand) {
        System.out.println("Player One Best Combination: " + playerOneBestHand.getPriority() + " with cards: " + playerOneBestHand.getCards());
        System.out.println("Player Two Best Combination: " + playerTwoBestHand.getPriority() + " with cards: " + playerTwoBestHand.getCards());
    }

    public List<Card> parseCards(String cardsStr) {
        List<Card> cards = new ArrayList<>();
        int i = 0;
        while (i < cardsStr.length()) {
            if (i + 2 < cardsStr.length() && cardsStr.substring(i, i + 2).equals("10")) {
            String cardStr = cardsStr.substring(i, i + 3);
            cards.add(Card.fromString(cardStr));
            i += 3;
        } else{
            String cardStr = cardsStr.substring(i, i + 2);
            cards.add(Card.fromString(cardStr));
            i += 2;
        }
    }
    return cards;
    }
}

