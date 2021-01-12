package it.unipi.softgram.utilities.enumerators;

public class Relation {
    public enum RelationType {
        FOLLOW,
        DEVELOP
    }


    public static String getRelationString(Relation.RelationType type){
        String relationString;
        switch (type){
            case FOLLOW:
                relationString = "FOLLOW";
                break;
            case DEVELOP:
                relationString = "DEVELOP";
                break;
            default:
                throw new RuntimeException("Type not set correctly");
        }
        return relationString;
    }
}