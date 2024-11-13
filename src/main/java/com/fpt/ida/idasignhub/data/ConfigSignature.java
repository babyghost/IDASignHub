package com.fpt.ida.idasignhub.data;

import org.springframework.web.multipart.MultipartFile;

public class ConfigSignature {

   int xLocation;
   int yLocation;
   int widthSignature;
   int heightSignature;
   int pageSign; //0 = first_page, 1 = last_page, 2 = custom_page
   int fontSizeSignature;

   String coutryLocation;
   String imgSignBase64;
   String cryptoStandard; // CMS or CADES
   String timeStampServer;
   String signatureType; //loại chữ ký. KY_COQUAN. KY_SAOY; KY_SAOLUC; KY_TRICKSAO; KY_CANHAN; KY_PHULUCVANBAN; KY_BANHANH; KY_YKIEN
   ShowSignature showSignature;
   String reasonSign;
   String locationSign;
   public ConfigSignature() {

   }

   public String getLocationSign() {
      return locationSign;
   }

   public void setLocationSign(String locationSign) {
      this.locationSign = locationSign;
   }

   public String getReasonSign() {
      return reasonSign;
   }

   public void setReasonSign(String reasonSign) {
      this.reasonSign = reasonSign;
   }

   public ConfigSignature(int xLocation, int yLocation, int widthSignature, int heightSignature, int pageSign, int fontSizeSignature, String coutryLocation, String imgSignBase64, String cryptoStandard, String timeStampServer, String signatureType, ShowSignature showSignature) {
      this.xLocation = xLocation;
      this.yLocation = yLocation;
      this.widthSignature = widthSignature;
      this.heightSignature = heightSignature;
      this.pageSign = pageSign;
      this.fontSizeSignature = fontSizeSignature;
      this.coutryLocation = coutryLocation;
      this.imgSignBase64 = imgSignBase64;
      this.cryptoStandard = cryptoStandard;
      this.timeStampServer = timeStampServer;
      this.signatureType = signatureType;
      this.showSignature = showSignature;
   }

   public int getxLocation() {
      return xLocation;
   }

   public void setxLocation(int xLcation) {
      this.xLocation = xLcation;
   }

   public int getyLocation() {
      return yLocation;
   }

   public void setyLocation(int yLocation) {
      this.yLocation = yLocation;
   }

   public int getWidthSignature() {
      return widthSignature;
   }

   public void setWidthSignature(int widthSignature) {
      this.widthSignature = widthSignature;
   }

   public int getHeightSignature() {
      return heightSignature;
   }

   public void setHeightSignature(int heightSignature) {
      this.heightSignature = heightSignature;
   }

   public int getPageSign() {
      return pageSign;
   }

   public void setPageSign(int pageSign) {
      this.pageSign = pageSign;
   }

   public int getFontSizeSignature() {
      return fontSizeSignature;
   }

   public void setFontSizeSignature(int fontSizeSignature) {
      this.fontSizeSignature = fontSizeSignature;
   }

   public String getCoutryLocation() {
      return coutryLocation;
   }

   public void setCoutryLocation(String coutryLocation) {
      this.coutryLocation = coutryLocation;
   }

   public String getImgSignBase64() {
      return imgSignBase64;
   }

   public void setImgSignBase64(String imgPath) {
      this.imgSignBase64 = imgPath;
   }

   public String getCryptoStandard() {
      return cryptoStandard;
   }

   public void setCryptoStandard(String cryptoStandard) {
      this.cryptoStandard = cryptoStandard;
   }

   public String getTimeStampServer() {
      return timeStampServer;
   }

   public void setTimeStampServer(String timeStampServer) {
      this.timeStampServer = timeStampServer;
   }

   public String getSignatureType() {
      return signatureType;
   }

   public void setSignatureType(String signatureType) {
      this.signatureType = signatureType;
   }

   public ShowSignature getShowSignature() {
      return showSignature;
   }

   public void setShowSignature(ShowSignature showSignature) {
      this.showSignature = showSignature;
   }

   @Override
   public String toString() {
      return "ConfigSignature{" +
              "xLocation=" + xLocation +
              ", yLocation=" + yLocation +
              ", widthSignature=" + widthSignature +
              ", heightSignature=" + heightSignature +
              ", pageSign=" + pageSign +
              ", fontSizeSignature=" + fontSizeSignature +
              ", coutryLocation='" + coutryLocation + '\'' +
              ", cryptoStandard='" + cryptoStandard + '\'' +
              ", timeStampServer='" + timeStampServer + '\'' +
              ", SignatureType='" + signatureType + '\'' +
              ", showSignature=" + showSignature +
              '}';
   }

}
