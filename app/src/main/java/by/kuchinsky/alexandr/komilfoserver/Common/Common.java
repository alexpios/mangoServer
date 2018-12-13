package by.kuchinsky.alexandr.komilfoserver.Common;

import by.kuchinsky.alexandr.komilfoserver.Model.User;

public class Common {
    public static User currentUser;

    public static final String UPDATE="Обновить";
    public static final String DELETE="Удалить";

    public  static final int PICK_IMAGE_REQUEST=71;

    public static String convertCodeToStatus(String code){

        if (code.equals("0"))
            return "Заявка оставлена";

        else if (code.equals("1"))
            return "Проверяется колличество мест";

        else
            return "Место забронировано";

    }


}
