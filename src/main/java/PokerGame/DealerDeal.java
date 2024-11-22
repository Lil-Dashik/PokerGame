package PokerGame;

import java.util.ArrayList;
import java.util.List;

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
        if (parseCards(playerOne).size() !=2 || parseCards(playerTwo).size() !=2) {
            throw new IllegalStateException("Each player must have exactly 2 cards.");
        }
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
        if (parseCards(flop).size() !=3) {
            throw new IllegalStateException("Each flop must have exactly 3 cards.");
        }
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
        if (parseCards(turn).size() !=1) {
            throw new IllegalStateException("Each turn must have exactly one card.");
        }
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
        if (parseCards(river).size() !=1) {
            throw new IllegalStateException("Each river must have exactly one card.");
        }
        Board newBoard = new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), board.getTurn(), river);
        PokerValidator.validateBoard(newBoard); // Проверка корректности
        return newBoard;
    }


    @Override
    public PokerResult decideWinner(Board board) throws InvalidPokerBoardException {
       if (board.getPlayerOne() == null || board.getPlayerTwo()  == null || board.getFlop() == null || board.getTurn() == null || board.getRiver()== null) {
           throw new InvalidPokerBoardException("Can't decide winner before the river is dealt.");
       }
        PokerValidator.printValidationMessageIfNeeded();


        List<Card> playerOneCards = parseCards(board.getPlayerOne());
        List<Card> playerTwoCards = parseCards(board.getPlayerTwo());
        List<Card> communityCards = getCommunityCards(board);

        HandPlayer playerOneBestHand = PokerHandEvaluator.evaluateBestHand(playerOneCards, communityCards);
        HandPlayer playerTwoBestHand = PokerHandEvaluator.evaluateBestHand(playerTwoCards, communityCards);

        printBestHands(playerOneBestHand, playerTwoBestHand);
        PokerResult result = determineWinner(playerOneBestHand, playerTwoBestHand, playerOneCards, playerTwoCards);
        printResult(result);
        PokerValidator.validateBoard(board);
        return result;
    }
    public PokerResult determineWinner(HandPlayer playerOneHand, HandPlayer playerTwoHand, List<Card> playerOneCards, List<Card> playerTwoCards) {
        int comparison = playerOneHand.compareTo(playerTwoHand);

        if (comparison > 0) {
            return PokerResult.PLAYER_ONE_WIN;
        } else if (comparison < 0) {
            return PokerResult.PLAYER_TWO_WIN;
        } else {
            return PokerResult.DRAW;
        }
    }
    public static int compareHands(HandPlayer hand1, HandPlayer hand2, List<Card> player1Cards, List<Card> player2Cards) {
        // Сравнение приоритетов комбинаций
        int priorityComparison = hand1.getPriority().compareTo(hand2.getPriority());
        if (priorityComparison != 0) {
            return priorityComparison;
        }

        // Если это старшая карта, сравниваем только карты игроков
        if (hand1.getPriority() == Priority.HIGH_CARD) {
            return comparePlayerCardsOnly(player1Cards, player2Cards);
        }

        // Логика для других комбинаций (например, пары, сеты и т.д.)
        List<Card> sortedHand1 = hand1.getCards().stream()
                .sorted((a, b) -> Integer.compare(b.getRankValue(), a.getRankValue()))
                .toList();
        List<Card> sortedHand2 = hand2.getCards().stream()
                .sorted((a, b) -> Integer.compare(b.getRankValue(), a.getRankValue()))
                .toList();

        for (int i = 0; i < Math.min(sortedHand1.size(), sortedHand2.size()); i++) {
            int rankComparison = Integer.compare(sortedHand1.get(i).getRankValue(), sortedHand2.get(i).getRankValue());
            if (rankComparison != 0) {
                return rankComparison;
            }
        }

        // Полностью равные комбинации
        return 0;
    }

    // Метод для сравнения только двух карт игроков
    private static int comparePlayerCardsOnly(List<Card> player1Cards, List<Card> player2Cards) {
        // Сортируем только две карты игроков
        List<Card> sortedPlayer1Cards = player1Cards.stream()
                .sorted((a, b) -> Integer.compare(b.getRankValue(), a.getRankValue()))
                .toList();
        List<Card> sortedPlayer2Cards = player2Cards.stream()
                .sorted((a, b) -> Integer.compare(b.getRankValue(), a.getRankValue()))
                .toList();

        // Сравниваем первую карту
        int firstCardComparison = Integer.compare(
                sortedPlayer1Cards.get(0).getRankValue(),
                sortedPlayer2Cards.get(0).getRankValue()
        );
        if (firstCardComparison != 0) {
            return firstCardComparison;
        }

        // Если первые карты равны, сравниваем вторую карту
        int secondCardComparison = Integer.compare(
                sortedPlayer1Cards.get(1).getRankValue(),
                sortedPlayer2Cards.get(1).getRankValue()
        );
        if (secondCardComparison != 0) {
            return secondCardComparison;
        }

        // Полностью равные карты игроков
        return 0;
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

        if (cardsStr == null || cardsStr.isEmpty()) {
            throw new IllegalArgumentException("Input string for cards cannot be null or empty.");
        }

        List<Card> cards = new ArrayList<>();
        int i = 0;

        while (i < cardsStr.length()) {
            // Определяем длину текущей карты
            if (i + 2 <= cardsStr.length() && cardsStr.substring(i, i + 2).equals("10")) {
                String cardStr = cardsStr.substring(i, i + 3); // Берем 3 символа для карты "10"
                cards.add(Card.fromString(cardStr));
                i += 3; // Переходим к следующей карте
            } else if (i + 2 <= cardsStr.length()) {
                String cardStr = cardsStr.substring(i, i + 2); // Берем 2 символа для обычной карты
                cards.add(Card.fromString(cardStr));
                i += 2; // Переходим к следующей карте
            } else {
                throw new IllegalArgumentException("Invalid card format in string: " + cardsStr);
            }
        }

        return cards;
    }
}

