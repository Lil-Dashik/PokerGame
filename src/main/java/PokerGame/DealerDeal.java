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
        String playerOne = deck.dealCard().toString() + " " + deck.dealCard().toString();
        String playerTwo = deck.dealCard().toString() + " " + deck.dealCard().toString();
        Board newBoard = new Board(playerOne, playerTwo);
        PokerValidator.validateBoard(newBoard); // Проверка корректности
        return newBoard;
    }

    @Override
    public Board dealFlop(Board board) {
        if (deck.size() < 3) {
            throw new IllegalStateException("Not enough cards to deal the flop.");
        }
        String flop = deck.dealCard().toString() + " " + deck.dealCard().toString() + " " + deck.dealCard().toString();
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
    public PokerResult decideWinner(Board board) {
        PokerValidator.printValidationMessageIfNeeded();
        List<Card> playerOneCards = parseCards(board.getPlayerOne());
        List<Card> playerTwoCards = parseCards(board.getPlayerTwo());

        List<Card> communityCards = parseCards(board.getFlop());
        if (board.getTurn() != null) communityCards.addAll(parseCards(board.getTurn()));
        if (board.getRiver() != null) communityCards.addAll(parseCards(board.getRiver()));

        // Находим лучшую комбинацию для каждого игрока
        HandPlayer playerOneBestHand = PokerHandEvaluator.evaluateBestHand(playerOneCards, communityCards);
        HandPlayer playerTwoBestHand = PokerHandEvaluator.evaluateBestHand(playerTwoCards, communityCards);

        // Определяем победителя
        int comparisonResult = playerOneBestHand.compareTo(playerTwoBestHand);
        if (comparisonResult > 0) {
            return PokerResult.PLAYER_ONE_WIN;
        } else if (comparisonResult < 0) {
            return PokerResult.PLAYER_TWO_WIN;
        } else {
            return PokerResult.DRAW;
        }
    }
    public Board analyzeExistingBoard(Board board) {
        PokerValidator.validateBoard(board); // Проверяем доску
        return board;
    }

    // Метод для создания новой доски на основе данных
    public Board createCustomBoard(String playerOne, String playerTwo, String flop, String turn, String river) {
        Board customBoard = new Board(playerOne, playerTwo, flop, turn, river);
        PokerValidator.validateBoard(customBoard); // Проверяем корректность
        return customBoard;
    }

    // Вспомогательный метод для парсинга строковых карт в объекты Card

    public List<Card> parseCards(String cardsStr) {
        List<Card> cards = new ArrayList<>();
        String[] cardTokens = cardsStr.split(" ");
        for (String token : cardTokens) {
            cards.add(Card.fromString(token));
        }
        return cards;
    }
}

