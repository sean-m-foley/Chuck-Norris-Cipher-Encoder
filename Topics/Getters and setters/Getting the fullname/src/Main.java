class User {
    private String firstName;
    private String lastName;

    public User() {
        this.firstName = "";
        this.lastName = "";
    }

    public static void main(String[] args) {
        User test = new User();
        test.setFirstName("Jim");
        test.setLastName("Shorts");
        System.out.println(test.getFullName());
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName != null ? firstName : "";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName != null ? lastName : "";
    }

    public String getFullName() {
        return firstName.isEmpty() && lastName.isEmpty() ? "Unknown" : firstName + " " + lastName;
    }
}
