package models;

public class User {
        private String name;
        private String lastName;
        private String rut;
        private String nHome;
        private String commune;
        private String region;
        private String phone;
        private String mail;
        private String street;
        private String userType;
        private String specilization;

    public User() {
    }


    public User(String name, String lastName, String rut, String nHome, String commune, String region, String phone, String mail, String userType, String specilization,String street) {
        this.name = name;
        this.lastName = lastName;
        this.rut = rut;
        this.nHome = nHome;
        this.commune = commune;
        this.region = region;
        this.phone = phone;
        this.mail = mail;
        this.userType = userType;
        this.specilization = specilization;
        this.street = street;
    }

    public User(String userId, String nombre, String apellido, String direccion, String numeroCasa, String comuna, String region, String telefono, String correo) {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getnHome() {
        return nHome;
    }

    public void setnHome(String nHome) {
        this.nHome = nHome;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSpecilization() {
        return specilization;
    }

    public void setSpecilization(String specilization) {
        this.specilization = specilization;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", rut='" + rut + '\'' +
                ", nHome='" + nHome + '\'' +
                ", commune='" + commune + '\'' +
                ", region='" + region + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", street='" + street + '\'' +
                ", userType='" + userType + '\'' +
                ", specilization='" + specilization + '\'' +
                '}';
    }
}
