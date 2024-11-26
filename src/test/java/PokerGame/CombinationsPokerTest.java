package PokerGame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Черви (Hearts)
//
//Красные масти, символизирующие сердце.
//Бубны (Diamonds)
//
//Красные масти, представляющие форму ромба.
//Трефы (Clubs)
//
//Черные масти, напоминающие клеверный лист.
//Пики (Spades)
//
//Черные масти, напоминающие форму наконечника копья.
public class CombinationsPokerTest {
    private Dealer dealer;

    @BeforeEach
    public void setUp() {
        dealer = new DealerDeal(); // Инициализируем объект Dealer
    }

    @Test
    public void testEvaluateBestHand() {
        List<Card> playerCards = Arrays.asList(
                new Card(Rank.ACE, Suit.H),
                new Card(Rank.KING, Suit.H)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.H),
                new Card(Rank.JACK, Suit.H),
                new Card(Rank.TEN, Suit.H),
                new Card(Rank.TWO, Suit.C),
                new Card(Rank.THREE, Suit.S)
        );

        HandPlayer bestHand = PokerHandEvaluator.evaluateBestHand(playerCards, communityCards);
        System.out.println("Best hand: " + bestHand);
        assertEquals(Priority.ROYAL_FLUSH, bestHand.getPriority());
    }

    @Test
    public void testHighCard() {
        Board board = new Board(
                "3H5D", // Player One
                "4S8C", // Player Two
                "AC7CKH", // Flop
                "2S", // Turn
                "QC" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_TWO_WIN, result); // Побеждает игрок с AHKH
    }

    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testOnePair() {
        Board board = new Board(
                "AD5H", // Player One
                "QS8C", // Player Two
                "AC7CKH", // Flop
                "2S", // Turn
                "QC" // River
        );

        PokerResult result = dealer.decideWinner(board);
        System.out.println("Result: " + result);// Побеждает игрок со старшей кикер-картой AC
    }

    //Черви (Hearts)
    //Бубны (Diamonds)
    //Трефы (Clubs)
    //Пики (Spades)
    @Test
    public void testTwoPair() {
        Board board = new Board(
                "7D6D", // Player One
                "6H5H", // Player Two
                "7C5C4S", // Flop
                "6S", // Turn
                "KH" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с KHQH
    }

    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testThreeOfAKind() {
        Board board = new Board(
                "AHAC", // Player One
                "QSQD", // Player Two
                "QH8CAS", // Flop
                "KD", // Turn
                "4C" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result);
    }

    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testStraight() {
        Board board = new Board(
                "9SJS", // Player One
                "9C10D", // Player Two
                "5S6H7C", // Flop
                "8D", // Turn
                "10S" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с старшей картой Straight 7C
    }

    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testFlush() {
        Board board = new Board(
                "ASKS", // Player One
                "3S7S", // Player Two
                "3D3H10S", // Flop
                "2S", // Turn
                "4S" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с Flush AH10H
    }

    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testFullHouse() {
        Board board = new Board(
                "9C9H", // Player One
                "6C6S", // Player Two
                "2C2D2S", // Flop
                "QS", // Turn
                "JD" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с старшей парой 6C6H
    }

    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testFourOfAKind() {
        Board board = new Board(
                "8C8H", // Player One
                "QDQC", // Player Two
                "8DQH4C", // Flop
                "9H", // Turn
                "8S" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с кикером AS
    }

    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testStraightFlush() {
        Board board = new Board(
                "6C10C", // Player One
                "6C5C", // Player Two
                "3C7C8C", // Flop
                "9C", // Turn
                "AC" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с старшей картой 8H9H
    }

    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testRoyalFlush() {
        Board board = new Board(
                "AH4H", // Player One
                "2H9H", // Player Two
                "5H10HQH", // Flop
                "KH", // Turn
                "JH" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result); // Побеждает игрок с AHKH
    }

    @Test
    public void ExceptionSameCards() {
        Board board = new Board("KDKS", "3D5H", "KH3S6C", "KS", "4S");
        assertThrows(InvalidPokerBoardException.class, () -> dealer.decideWinner(board));
    }

    @Test
    public void testParseCards() {
        String cardsStr = "KH KS QH JD 10C";
        List<Card> cards = Card.parseCards(cardsStr);

        List<Card> expectedCards = Arrays.asList(
                new Card(Rank.KING, Suit.H),
                new Card(Rank.KING, Suit.S),
                new Card(Rank.QUEEN, Suit.H),
                new Card(Rank.JACK, Suit.D),
                new Card(Rank.TEN, Suit.C)
        );

        assertEquals(expectedCards, cards);
    }

    @Test
    public void setWithKickerDraw(){
        Board board = new Board("2C7D", "2DJD", "2H2SQH", "QC", "AS");

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.DRAW, result);

    }
    @Test
    public void testParseCardsФ() {
        String cardsStr = "AD10H5S7C";
        List<Card> cards = Card.parseCards(cardsStr);

        assertEquals(4, cards.size());
        assertEquals(new Card(Rank.ACE, Suit.D), cards.get(0));
        assertEquals(new Card(Rank.TEN, Suit.H), cards.get(1));
        assertEquals(new Card(Rank.FIVE, Suit.S), cards.get(2));
        assertEquals(new Card(Rank.SEVEN, Suit.C), cards.get(3));
    }

    @Test
    public void testCardStringWithSpaces() {
        String cardsStr = "10H JD KH QH";
        List<Card> cards = Card.parseCards(cardsStr.replace(" ", ""));

        assertEquals(4, cards.size());
    }

    @Test
    public void testParseCardsWithSpaces() {
        String cardsStr = " 10H  JD KH QH ";
        List<Card> cards = Card.parseCards(cardsStr.trim().replaceAll(" +", ""));

        assertEquals(4, cards.size());
        assertEquals(new Card(Rank.TEN, Suit.H), cards.get(0));
        assertEquals(new Card(Rank.JACK, Suit.D), cards.get(1));
        assertEquals(new Card(Rank.KING, Suit.H), cards.get(2));
        assertEquals(new Card(Rank.QUEEN, Suit.H), cards.get(3));
    }


    @Test
    public void testEmptyCardString() {
        String cardsStr = "";
        assertThrows(IllegalArgumentException.class, () -> Card.parseCards(cardsStr));
    }

    @Test
    public void testParseCardsWithExtraSpaces() {
        String cardsStr = " KH   KS  QH JD   10C  ";
        List<Card> cards = Card.parseCards(cardsStr);

        assertEquals(5, cards.size());
        assertEquals(new Card(Rank.KING, Suit.H), cards.get(0));
        assertEquals(new Card(Rank.KING, Suit.S), cards.get(1));
        assertEquals(new Card(Rank.QUEEN, Suit.H), cards.get(2));
        assertEquals(new Card(Rank.JACK, Suit.D), cards.get(3));
        assertEquals(new Card(Rank.TEN, Suit.C), cards.get(4));
    }


    @Test
    public void royalFlashBoth() {
        Board board = new Board("2D3S", "3D3H", "QSJSKS", "10S", "AS");
        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.DRAW, result);
    }


    @Test
    public void testParseCardsIntegration() {
        Board board = new Board("KH KS", "QS JD", "10H 9H 8H", "7H", "6H");

    }




    @Test
    public void testOnePairComparison() {
        Board board = new Board(
                "5C5H", // Player One
                "8C3H", // Player Two
                "AC7CKH", // Flop
                "2S", // Turn
                "QC" // River
        );

        PokerResult result = dealer.decideWinner(board);

        assertEquals(PokerResult.PLAYER_TWO_WIN, result, "Player Two should win due to higher kicker (8 vs 7).");
    }

    @Test
    public void testRoyalFlushDraw() {
        Board board = new Board(
                "3S4S",      // Player One Cards
                "ASKS",      // Player Two Cards
                "QSJS10S",   // Flop
                null,        // Turn (not needed)
                null         // River (not needed)
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.DRAW, result, "Both players have a Royal Flush; should result in a DRAW.");
    }

    @Test
    public void testOnePairKicker() {
        Board board = new Board(
                "QD5H", // Player One
                "QS8C", // Player Two
                "AC7CKH", // Flop
                "2S", // Turn
                "QC" // River
        );

        PokerResult result = dealer.decideWinner(board);

        // Лучшая рука Player One: QD QC AC KH 7C (Одна пара, кикеры A, K, 7)
        // Лучшая рука Player Two: QS QC AC KH 8C (Одна пара, кикеры A, K, 8)
        // Player Two выигрывает благодаря кикеру 8C
        assertEquals(PokerResult.PLAYER_TWO_WIN, result, "Player Two should win because of the higher kicker.");
    }

    @Test
    public void testTwoPairKicker() {
        Board board = new Board(
                "KD2H", // Player One
                "KC3H", // Player Two
                "KH2C3D", // Flop
                "4S", // Turn
                "5C" // River
        );

        PokerResult result = dealer.decideWinner(board);

        // Лучшая рука Player One: KD KH 2H 2C 5C (Две пары, кикер 5)
        // Лучшая рука Player Two: KC KH 3H 3D 5C (Две пары, кикер 5)
        // Player Two выигрывает благодаря старшей второй паре 3 > 2
        assertEquals(PokerResult.PLAYER_TWO_WIN, result, "Player Two should win because of the higher second pair.");
    }

    @Test
    public void testThreeOfAKindKicker() {
        Board board = new Board(
                "5D5H", // Player One
                "5C3D", // Player Two
                "5S10C8D", // Flop
                "2S", // Turn
                "AC" // River
        );

        PokerResult result = dealer.decideWinner(board);

        // Лучшая рука Player One: 5D 5H 5S AC 8D (Тройка, кикеры A, 8)
        // Лучшая рука Player Two: 5C 5S 5D AC 7C (Тройка, кикеры A, 7)
        // Player One выигрывает благодаря старшему кикеру 8 > 7
        assertEquals(PokerResult.PLAYER_ONE_WIN, result, "Player One should win because of the higher kicker.");
    }

    @Test
    public void testStraightKicker() {
        Board board = new Board(
                "6D7H", // Player One
                "6C7S", // Player Two
                "8C9D10H", // Flop
                "JS", // Turn
                "3S" // River
        );

        PokerResult result = dealer.decideWinner(board);

        // Лучшая рука Player One: 6D 7H 8C 9D 10H (Стрит до 10)
        // Лучшая рука Player Two: 6C 7S 8C 9D 10H (Стрит до 10)
        // Оба игрока имеют идентичный стрит, поэтому ничья
        assertEquals(PokerResult.DRAW, result, "The result should be a DRAW as both players have the same Straight.");
    }

    @Test
    public void testFlushKicker() {
        Board board = new Board(
                "2S4S", // Player One
                "3S5S", // Player Two
                "6S8S10S", // Flop
                "KS", // Turn
                "AS" // River
        );

        PokerResult result = dealer.decideWinner(board);

        // Лучшая рука Player One: AS KS 10S 8S 6S (Флеш, старшая карта A)
        // Лучшая рука Player Two: AS KS 10S 8S 6S (Флеш, старшая карта A)
        // Оба игрока имеют идентичный флеш, поэтому ничья
        assertEquals(PokerResult.DRAW, result, "The result should be a DRAW as both players have the same Flush.");
    }

    @Test
    public void testFlushKickerф() {
        Board board = new Board(
                "3HKH", // Player One
                "AHQH", // Player Two
                "JH9H8H", // Flop
                "2H", // Turn
                "7H" // River
        );

        PokerResult result = dealer.decideWinner(board);

        // Player One Best Combination: AHKHJH9H8H (Flush with Ace, King kicker)
        // Player Two Best Combination: AHQHJH9H8H (Flush with Ace, Queen kicker)
        assertEquals(PokerResult.PLAYER_TWO_WIN, result, "Player One should win because of the higher kicker in the flush.");
    }

    //Черви (Hearts) красные
    //Бубны (Diamonds) голубые
    //Трефы (Clubs) зелёные
    //Пики (Spades) чёрные
    @Test
    public void testFullHouseKicker() {
        Board board = new Board(
                "KCKH", // Player One
                "KDQC", // Player Two
                "KSQSQH", // Flop
                "7C", // Turn
                "3S" // River
        );

        PokerResult result = dealer.decideWinner(board);

        // Player One Best Combination: KDKHKSQSQH (Full House, Kings over Queens)
        // Player Two Best Combination: KDQCQSQHKS (Full House, Queens over Kings)
        assertEquals(PokerResult.PLAYER_ONE_WIN, result, "Player One should win because of the stronger Full House.");
    }

    @Test
    public void testFourOfAKindKicker() {
        Board board = new Board(
                "QCJH", // Player One
                "3H10S", // Player Two
                "8C8H8D", // Flop
                "8S", // Turn
                "AC" // River
        );

        PokerResult result = dealer.decideWinner(board);

        // Player One Best Combination: 8C8H8S8D AC (Four of a Kind with Ace kicker)
        // Player Two Best Combination: 8C8H8S8D 7C (Four of a Kind with 7 kicker)
        assertEquals(PokerResult.DRAW, result, "Player One should win because of the higher kicker.");
    }

    @Test
    public void testStraightFlushKicker() {
        Board board = new Board(
                "8H7H", // Player One
                "8C6C", // Player Two
                "9H10HJH", // Flop
                "2C", // Turn
                "AC" // River
        );

        PokerResult result = dealer.decideWinner(board);

        // Player One Best Combination: 8H7H6H5H4H
        // Player Two Best Combination: 8H6H5H4H3H
        assertEquals(PokerResult.PLAYER_ONE_WIN, result, "Player One should win because of the higher Straight Flush.");
    }

    @Test
    public void testRoyalFlushKicker() {
        Board board = new Board(
                "AHKH", // Player One
                "AC5H", // Player Two
                "JH10HQH", // Flop
                "8H", // Turn
                "7H" // River
        );

        PokerResult result = dealer.decideWinner(board);
        assertEquals(PokerResult.PLAYER_ONE_WIN, result, "Both players have the same Royal Flush; it should be a draw.");
    }

}

