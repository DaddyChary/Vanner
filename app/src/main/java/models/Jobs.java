package models;

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

    public Jobs() {
    }

    public Jobs(String id, String title,String description,String salary,String vacancies,String mode,String deadline,String companyName,String mail) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVacancies() {
        return vacancies;
    }

    public void setVacancies(String vacancies) {
        this.vacancies = vacancies;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
