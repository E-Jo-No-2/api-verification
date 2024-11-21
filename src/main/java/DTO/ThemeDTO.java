package DTO;

public class ThemeDTO {

    private String themeName;
    private String description;

    // 생성자
    public ThemeDTO(String themeName, String description) {
        this.themeName = themeName;
        this.description = description;
    }

    // Getter and Setter
    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
