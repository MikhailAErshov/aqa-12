import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryDto {
    private Integer totvs;
    private Integer sold;
    private Integer string;
    private Integer available;
    private Integer awaiable;
    private Integer soldout;
    private Integer out;

    public Integer getTotvs() {
        return totvs;
    }

    public void setTotvs(Integer totvs) {
        this.totvs = totvs;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Integer getString() {
        return string;
    }

    public void setString(Integer string) {
        this.string = string;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getAwaiable() {
        return awaiable;
    }

    public void setAwaiable(Integer awaiable) {
        this.awaiable = awaiable;
    }

    public Integer getSoldout() {
        return soldout;
    }

    public void setSoldout(Integer soldout) {
        this.soldout = soldout;
    }

    public Integer getOut() {
        return out;
    }

    public void setOut(Integer out) {
        this.out = out;
    }
}
