package dto;

public class QrDto {
    private String qrCode;
    public QrDto(){

    }
    public QrDto(String qr){
        this.qrCode = qr;
    }
    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        qrCode = qrCode;
    }
}
