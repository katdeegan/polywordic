package com.ooad_kd_yz.polywordic.repository;

// In-memory implementation of IWordRepository to generate random 5-letter word for Polywordic games.

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class WordRepository implements IWordRepository {
    private final List<String> words;
    private final Set<String> wordSet;
    private final Random random;

    public WordRepository() {
        this.random = new Random();
        this.words = Arrays.asList(
                "ABOUT", "ABOVE", "ABUSE", "ACTOR", "ACUTE",
                "ADMIT", "ADOPT", "ADULT", "AFTER", "AGAIN",
                "AGENT", "AGREE", "AHEAD", "ALARM", "ALBUM",
                "ALERT", "ALIKE", "ALIVE", "ALLOW", "ALONE",
                "ALONG", "ALTER", "ANGEL", "ANGER", "ANGLE",
                "ANGRY", "APART", "APPLE", "APPLY", "ARENA",
                "ARGUE", "ARISE", "ARRAY", "ASIDE", "ASSET",
                "AUDIO", "AVOID", "AWAKE", "AWARD", "AWARE",
                "BADLY", "BAKER", "BASES", "BASIC", "BASIS",
                "BEACH", "BEGAN", "BEGIN", "BEGUN", "BEING",
                "BELOW", "BENCH", "BILLY", "BIRTH", "BLACK",
                "BLAME", "BLIND", "BLOCK", "BLOOD", "BOARD",
                "BOOST", "BOOTH", "BOUND", "BRAIN", "BRAND",
                "BREAD", "BREAK", "BREED", "BRIEF", "BRING",
                "BROAD", "BROKE", "BROWN", "BUILD", "BUILT",
                "BUYER", "CABLE", "CALIF", "CARRY", "CATCH",
                "CAUSE", "CHAIN", "CHAIR", "CHAOS", "CHARM",
                "CHART", "CHASE", "CHEAP", "CHECK", "CHEST",
                "CHIEF", "CHILD", "CHINA", "CHOSE", "CIVIL",
                "CLAIM", "CLASS", "CLEAN", "CLEAR", "CLICK",
                "CLOCK", "CLOSE", "COACH", "COAST", "COULD",
                "COUNT", "COURT", "COVER", "CRAFT", "CRASH",
                "CRAZY", "CREAM", "CRIME", "CROSS", "CROWD",
                "CROWN", "CRUDE", "CURVE", "CYCLE", "DAILY",
                "DANCE", "DATED", "DEALT", "DEATH", "DEBUT",
                "DELAY", "DEPTH", "DOING", "DOUBT", "DOZEN",
                "DRAFT", "DRAMA", "DRANK", "DRAWN", "DREAM",
                "DRESS", "DRILL", "DRINK", "DRIVE", "DROVE",
                "DYING", "EAGER", "EARLY", "EARTH", "EIGHT",
                "EIGHT", "ELECT", "ELITE", "EMPTY", "ENEMY",
                "ENJOY", "ENTER", "ENTRY", "EQUAL", "ERROR",
                "EVENT", "EVERY", "EXACT", "EXIST", "EXTRA",
                "FAITH", "FALSE", "FAULT", "FIBER", "FIELD",
                "FIFTH", "FIFTY", "FIGHT", "FINAL", "FIRST",
                "FIXED", "FLASH", "FLEET", "FLOOR", "FLUID",
                "FOCUS", "FORCE", "FORTH", "FORTY", "FORUM",
                "FOUND", "FRAME", "FRANK", "FRAUD", "FRESH",
                "FRONT", "FRUIT", "FULLY", "FUNNY", "GIANT",
                "GIVEN", "GLASS", "GLOBE", "GOING", "GRACE",
                "GRADE", "GRAND", "GRANT", "GRASS", "GREAT",
                "GREEN", "GROSS", "GROUP", "GROWN", "GUARD",
                "GUESS", "GUEST", "GUIDE", "HAPPY", "HARRY",
                "HEART", "HEAVY", "HELLO", "HENCE", "HENRY",
                "HORSE", "HOTEL", "HOUSE", "HUMAN", "IDEAL",
                "IMAGE", "INDEX", "INNER", "INPUT", "ISSUE",
                "JAPAN", "JIMMY", "JOINT", "JONES", "JUDGE",
                "KNOWN", "LABEL", "LARGE", "LASER", "LATER",
                "LAUGH", "LAYER", "LEARN", "LEASE", "LEAST",
                "LEAVE", "LEGAL", "LEMON", "LEVEL", "LEWIS",
                "LIGHT", "LIMIT", "LINKS", "LIVES", "LOCAL",
                "LOGIC", "LOOSE", "LOWER", "LUCKY", "LUNCH",
                "LYING", "MAGIC", "MAJOR", "MAKER", "MARCH",
                "MARIA", "MATCH", "MAYBE", "MAYOR", "MEANT",
                "MEDIA", "METAL", "MIGHT", "MINOR", "MINUS",
                "MIXED", "MODEL", "MONEY", "MONTH", "MORAL",
                "MOTOR", "MOUNT", "MOUSE", "MOUTH", "MOVIE",
                "MUSIC", "NEEDS", "NEVER", "NEWLY", "NIGHT",
                "NOISE", "NORTH", "NOTED", "NOVEL", "NURSE",
                "OCCUR", "OCEAN", "OFFER", "OFTEN", "ORDER",
                "OTHER", "OUGHT", "PAINT", "PANEL", "PAPER",
                "PARTY", "PEACE", "PETER", "PHASE", "PHONE",
                "PHOTO", "PIECE", "PILOT", "PITCH", "PLACE",
                "PLAIN", "PLANE", "PLANT", "PLATE", "POINT",
                "POUND", "POWER", "PRESS", "PRICE", "PRIDE",
                "PRIME", "PRINT", "PRIOR", "PRIZE", "PROOF",
                "PROUD", "PROVE", "QUEEN", "QUICK", "QUIET",
                "QUITE", "RADIO", "RAISE", "RANGE", "RAPID",
                "RATIO", "REACH", "READY", "REFER", "RIGHT",
                "RIVAL", "RIVER", "ROBIN", "ROGER", "ROMAN",
                "ROUGH", "ROUND", "ROUTE", "ROYAL", "RURAL",
                "SCALE", "SCENE", "SCOPE", "SCORE", "SENSE",
                "SERVE", "SEVEN", "SHALL", "SHAPE", "SHARE",
                "SHARP", "SHEET", "SHELF", "SHELL", "SHIFT",
                "SHINE", "SHIRT", "SHOCK", "SHOOT", "SHORT",
                "SHOWN", "SIGHT", "SINCE", "SIXTH", "SIXTY",
                "SIZED", "SKILL", "SLEEP", "SLIDE", "SMALL",
                "SMART", "SMILE", "SMITH", "SMOKE", "SOLID",
                "SOLVE", "SORRY", "SOUND", "SOUTH", "SPACE",
                "SPARE", "SPEAK", "SPEED", "SPEND", "SPENT",
                "SPLIT", "SPOKE", "SPORT", "STAFF", "STAGE",
                "STAKE", "STAND", "START", "STATE", "STEAM",
                "STEEL", "STICK", "STILL", "STOCK", "STONE",
                "STOOD", "STORE", "STORM", "STORY", "STRIP",
                "STUCK", "STUDY", "STUFF", "STYLE", "SUGAR",
                "SUITE", "SUPER", "SWEET", "TABLE", "TAKEN",
                "TASTE", "TAXES", "TEACH", "TERRY", "TEXAS",
                "THANK", "THEFT", "THEIR", "THEME", "THERE",
                "THESE", "THICK", "THING", "THINK", "THIRD",
                "THOSE", "THREE", "THREW", "THROW", "TIGHT",
                "TIMES", "TITLE", "TODAY", "TOPIC", "TOTAL",
                "TOUCH", "TOUGH", "TOWER", "TRACK", "TRADE",
                "TRAIN", "TREAT", "TREND", "TRIAL", "TRIED",
                "TRIES", "TRUCK", "TRULY", "TRUST", "TRUTH",
                "TWICE", "UNDER", "UNDUE", "UNION", "UNITY",
                "UNTIL", "UPPER", "URBAN", "USAGE", "USUAL",
                "VALID", "VALUE", "VIDEO", "VIRUS", "VISIT",
                "VITAL", "VOCAL", "VOICE", "WASTE", "WATCH",
                "WATER", "WHEEL", "WHERE", "WHICH", "WHILE",
                "WHITE", "WHOLE", "WHOSE", "WOMAN", "WORLD",
                "WORRY", "WORSE", "WORST", "WORTH", "WOULD",
                "WOUND", "WRITE", "WRONG", "WROTE", "YIELD",
                "YOUNG", "YOUTH"
        );
        this.wordSet = new HashSet<>(words); // HashSet ensures uniqueness (not thread-safe if multiple threads are trying to access its elements)
    }

    @Override
    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

    @Override
    public boolean isValidWord(String word) {
        return word != null && wordSet.contains(word.toUpperCase()) && word.length() == 5;
    }

    @Override
    public List<String> getAllWords() {
        return new ArrayList<>(words);
    }
}
