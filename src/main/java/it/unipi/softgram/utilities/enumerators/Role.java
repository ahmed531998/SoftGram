package it.unipi.softgram.utilities.enumerators;


public class Role {
    public enum RoleValue {
        NORMAL_USER,
        DEVELOPER,
        ADMIN
    }

    public static RoleValue getRoleFromString(String role){
        switch (role){
            case "Admin":
                return RoleValue.ADMIN;
            case "Developer":
                return RoleValue.DEVELOPER;
            case "Normal User":
                return RoleValue.NORMAL_USER;
            default:
                throw new RuntimeException("Role not set correctly");
        }
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
