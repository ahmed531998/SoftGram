package it.unipi.softgram.utilities.enumerators;


public class Role {
    public enum RoleValue {
        NORMAL_USER,
        DEVELOPER,
        ADMIN
    }

    public static String getRoleString(RoleValue role){
        String roleString;
        switch (role){
            case ADMIN:
                roleString = "Admin";
                break;
            case DEVELOPER:
                roleString = "Developer";
                break;
            case NORMAL_USER:
                roleString = "Normal User";
                break;
            default:
                throw new RuntimeException("Role not set correctly");
        }
        return roleString;
    }
}
