package models;

import com.google.firebase.database.PropertyName;

public class Jobs {
    private String id;
    private String title;
    private String description;
    private String salary;
    private String vacancies;
    private String mode;
    private String deadline;
    private String companyName;
    private String mail;

    // Constructor vacío necesario para Firebase
    public Jobs() {
    }

    // Constructor completo
    public Jobs(String id, String title, String description, String salary, String vacancies,
                String mode, String deadline, String companyName, String mail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.vacancies = vacancies;
        this.mode = mode;
        this.deadline = deadline;
        this.companyName = companyName;
        this.mail = mail;
    }

    // Getters y setters
    @PropertyName("id")
    public String getId() {
        return id;
    }

    @PropertyName("id")
    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    @PropertyName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("salary")
    public String getSalary() {
        return salary;
    }

    @PropertyName("salary")
    public void setSalary(String salary) {
        this.salary = salary;
    }

    @PropertyName("vacancies")
    public String getVacancies() {
        return vacancies;
    }

    @PropertyName("vacancies")
    public void setVacancies(String vacancies) {
        this.vacancies = vacancies;
    }

    @PropertyName("mode")
    public String getMode() {
        return mode;
    }

    @PropertyName("mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    @PropertyName("deadline")
    public String getDeadline() {
        return deadline;
    }

    @PropertyName("deadline")
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @PropertyName("companyName")
    public String getCompanyName() {
        return companyName;
    }

    @PropertyName("companyName")
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @PropertyName("mail")
    public String getMail() {
        return mail;
    }

    @PropertyName("mail")
    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "Jobs{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", salary='" + salary + '\'' +
                ", vacancies='" + vacancies + '\'' +
                ", mode='" + mode + '\'' +
                ", deadline='" + deadline + '\'' +
                ", companyName='" + companyName + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
