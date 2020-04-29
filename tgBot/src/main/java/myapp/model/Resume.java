package myapp.model;

public class Resume {
    String tgUsername;
    String fullName;
    String age;
    String telephone;
    String skypeLogin;

    public String getTgUsername() {
        return tgUsername;
    }

    public void setTgUsername(String tgUsername) {
        this.tgUsername = tgUsername;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSkypeLogin() {
        return skypeLogin;
    }

    public void setSkypeLogin(String skypeLogin) {
        this.skypeLogin = skypeLogin;
    }

    @Override
    public String toString() {
        return "Новое резюме от " +
                "tgUsername: " + tgUsername +":"+
                "\n ФИО: " + fullName +
                "\n Возраст: " + age +
                "\n Телефон: " + telephone +
                "\n skype: " + skypeLogin ;
    }
}
