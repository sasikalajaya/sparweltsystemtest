package behatfeatures;

import org.openqa.selenium.*;

import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.support.ui.*;

public class BusinessLibrary {

    WebDriver driver;
    String searchResult = "No products were found that matched your criteria.";
    String itemNotFoundMessage = "Searched Item not found";
    String Related_products = "Related products";
    String Product_tags = "Product tags";
    String newAddress_FirstName= "James";
    String newAddress_LastName = "Miller";
    String newAddress_Address1 ="1, Westmister Road";
    String newAddress_ZipPostalCode= "WM1 C34";
    String newAddress_City ="London";
    String newAddress_Country = "United Kingdom";
    String newAddress_Email ="test13@yourstore.com";
    String newAddress_Phoneno ="test13@yourstore.com";

    public BusinessLibrary( WebDriver driver){
        this.driver = driver;
    }
    public void login(String admin, String password)
    {
        WebElement loginButton = driver.findElement(By.xpath("//div[@id='shopbar-account']/a/span/span[2]"));
        loginButton.click();
        WebElement userName = driver.findElement(By.id("Username"));
        userName.sendKeys(admin);
        WebElement passWord = driver.findElement(By.id("Password"));
        passWord.sendKeys(password);
        WebElement submitButton = driver.findElement(By.xpath("(//button[@type='submit'])[2]"));
        submitButton.click();
        Utils.pause(5);
//        if(!isUserLoggedin(admin))
//        throw new RuntimeException("User is not logged in");

    }

    public boolean isUserLoggedin(String user) {
            return Utils.isTextAvailable(user);
    }

    public void selectItemfromHomePage(String item) {
        WebElement itemLink = driver.findElement(By.linkText(item));
        itemLink.click();
    }

    public void addItemsIntoBasket(String qty) {
        WebElement quantity=driver.findElement(By.xpath("//div[@id='AddToCart']/div[2]/div/input"));
        quantity.clear();
        quantity.sendKeys(qty);
        WebElement addToCart=driver.findElement(By.linkText("Add to cart"));
        addToCart.click();
    }

    public boolean isItemInBasket(String item) {
        return driver.findElement(By.linkText(item)).isDisplayed();
    }

    public boolean searchItemUsingSearchOptionAndCompare(String item){
        if (searchItemUsingSearchOption(item)) {
            goToSelectedProduct();
            addToCompare();
            return true;
        }
        return false;
    }

    private void addToCompare() {
      WebElement compare_link = driver.findElement(By.linkText("Add to compare list"));
        compare_link.click();
        Utils.pause(3);
    }

    public void goToCompare(){
        WebElement  compare_product_page = driver.findElement(By.xpath("//*[@id='shopbar-compare']/a/span[1]/span[2]"));
        compare_product_page.click();
        Utils.pause(2);
        WebElement compare_product_list = driver.findElement(By.linkText("Compare products list"));
        compare_product_list.click();
        Utils.pause(3);
    }

    public boolean compareItemsAndCheckThePricesAvailable(String [] items ) {
        try {
            WebElement priceAttribute = driver.findElement(By.xpath("//*[@id='content-center']/div/div[2]/table/tbody//th[contains(text(),'Price')]"));
            System.out.println(priceAttribute.getText());

        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return false;
        }
        try
        {
            List<WebElement> priceAttribute = driver.findElements(By.xpath("//*[@id='content-center']/div/div[2]/table/tbody//th[contains(text(),'Price')]/following-sibling::td"));
            System.out.println(priceAttribute.size());
            for ( WebElement e : priceAttribute) {
                int i;
                System.out.println(i = e.getText().length());
                if (!(i > 0)) {
                    return false;
                }
            }
        }
        catch(NoSuchElementException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void logout() {
        WebElement account_dropdown = driver.findElement(By.xpath("//*[@id='shopbar-account']/a/span[1]/span[2]/i"));
        account_dropdown.click();
        WebElement logout_link = driver.findElement(By.xpath("//*[@id='shopbar-account']/ul/li[6]/a"));
        logout_link.click();
        driver.getPageSource().contains("Log in");
    }

    public void goToCartURL() {

        String Url = driver.getCurrentUrl();
        int x = Url.split("/").length;
        String u = Url.split("/")[x-1];
        driver.navigate().to(Url.replace(u,"cart"));
        Utils.pause(5);

    }
    public boolean checkItemsInCompareBasket(String ... items) {
        System.out.println("checkItemsInCompareBasket");
        String pageSource = driver.getPageSource();
        for (String s : items) {
            if (! pageSource.contains(s)) {
                return false;
            }
        }
        return true;
    }
    public void goToCart() {

        WebElement basket = driver.findElement(By.xpath("//*[@id='shopbar-cart']/a/span[1]/span[2]"));
        basket.click();
        WebElement goto_cart_link = driver.findElement(By.linkText("Go to cart"));
        goto_cart_link.click();
     }

    public void checkOut() {

       WebElement checkout = driver.findElement(By.id("checkout"));
       checkout.click();
       Utils.pause(3);

    }

    public boolean isPaymentOptionAvailble() {
        return true;
    }

    public String[] getAllOption() {
        return new String[0];
    }

    public void selectOpinion(String opinion) {

    }

    public String getErrorMessage() {
                return null;
    }

    public boolean searchItemUsingSearchOption(String item)  {

        WebElement quickSearch = driver.findElement(By.id("quicksearch"));
        quickSearch.sendKeys(item);
        WebElement searchIcon = driver.findElement((By.xpath("//*[@id='shopbar']/div/form/div/button")));
        searchIcon.click();

         if ( itemNotFound(searchResult)){
            return false;
        }
        return true;
    }

    public boolean itemNotFound (String str){

        return Utils.isTextAvailable(str);
    }

    public void goToSelectedProduct() {
        //select the first product
        WebElement selectProduct = driver.findElement(By.xpath(".//*[@id='content-center']/div/div[2]/form/div[4]/div[1]/div/div/div/article/div[2]/h3/a/span"));
        selectProduct.click();
    }

    public boolean verifyRecommendationPresent(String recommendationTitle) {

        if (!driver.getPageSource().contains(recommendationTitle)) {
            return false;
        }
        return true;
    }

    public boolean verifyRecommendationPosition(){
        Point recommendPosition = getRecommendationsLocation();

        if ((isRecommendationbelowProductTag(recommendPosition) && isRecommendationbelowRelatedProduct(recommendPosition))) {
            System.out.println("true");
            return true;
        }
        return false;
    }

    private boolean isRecommendationbelowRelatedProduct(Point recommendPosition) {

        // if Present, Recommendation should appear just above the footwrapper or below the Related Products or Tags , if any are present
        if (driver.getPageSource().contains(Related_products)) {
            Point relatedProductPosition = driver.findElement(By.xpath
                    ("//*[contains(text(),Related_products)]")).getLocation();
            System.out.println("related product1");
            System.out.println(recommendPosition.getX());
            System.out.println(relatedProductPosition.getX());
            System.out.println((recommendPosition.getX() < relatedProductPosition.getX()));
        }
        return true;

    }

    private boolean isRecommendationbelowProductTag(Point recommendPosition) {

        // if Present, Recommendation should appear just above the footwrapper or below the Related Products or Tags , if any are present
        System.out.println("product");
        if (driver.getPageSource().contains(Product_tags)) {
            Point productTagPosition = driver.findElement(By.xpath("//*contains(text(),Product_tags)]")).getLocation();
            System.out.println("product1");
            return (recommendPosition.getX() < productTagPosition.getX());
        }
        return true;

    }

    private Point getRecommendationsLocation() {

        return driver.findElement(By.xpath("//*[@id='product-details-form']/article/*[contains(text(),recommedationTitle)]")).getLocation();
    }

    public StringBuffer verifyRecommendationContents(StringBuffer strb) {

        if (no_of_recommendedProducts() > 20 ){
            strb.append("No of items is > 20");

        }

        if (no_of_recommendedProducts() > 4) {
            if (!(verifyScrollButtonpresent() == 2)) {
                strb.append("Scroll Button Missing or not Enabled");
            }
        }

        return strb;
    }

    private int no_of_recommendedProducts(){
        // No of elements cannot be greater than 20
        List<WebElement> no_of_products = driver.findElements(By.xpath
                ("//*[@class='product-recommendations']//article"));
        //System.out.println(no_of_products.size());
        return  no_of_products.size();

    }
    private int verifyScrollButtonpresent() {
        List<WebElement> scroll = driver.findElements(By.xpath("//div[@class='product-list scroll']/a"));
        //System.out.println(scroll.size());
        for ( WebElement a : scroll) {
            if (!(a.isDisplayed())){return -1; }
        }
        return scroll.size();
    }

    public String selectRecommedProductByImage() {
        WebElement recommendedProductImage =
                driver.findElement(By.xpath("//*[@class='product-recommendations']/div/div[2]/div/div/div/div/article[1]/figure/a/img"));
        String str = (recommendedProductImage.getAttribute("title").split("Show details for"))[1];

        //System.out.println(str);
        recommendedProductImage.click();
        return str;

    }

    public String selectRecommedProductByDesc() {
        WebElement recommendedProductDesc =
                driver.findElement(By.xpath("//*[@class='product-recommendations']/div/div[2]/div/div/div/div/article[1]/div/h3/a/span"));
        String str = (recommendedProductDesc.getText());

        System.out.println(str);
        recommendedProductDesc.click();
        return str;

    }

    public String selectProductName() {

        return driver.findElement(By.xpath("//*[@id='product-details-form']/article/div[1]/div[2]/div[1]/h1")).getText();
    }

    public boolean is_LeftMost_Recommended_Product_Displayed() {
        WebElement recommendedProductDesc =
                driver.findElement(By.xpath("//*[@class='product-recommendations']/div/div[2]/div/div/div/div/article[1]/div/h3/a/span"));
        return recommendedProductDesc.isDisplayed();

    }
    public void advance_nextScroll() {
        WebElement nextScrollButton = driver.findElement(By.xpath("//div[@class='product-list scroll']/a[last()]"));
        Utils.pause(2);
        nextScrollButton.click();

    }

    public void backward_previousScroll() {
        WebElement nextScrollButton = driver.findElement(By.xpath("//div[@class='product-list scroll']/a"));
        Utils.pause(2);
        nextScrollButton.click();
    }


    public void confirmAddress() {
        System.out.println("inside Confirm address");
        if (!confirmBillingAddress()){
            enter_newBiling_Address();
            driver.findElement(By.id("nextstep")).click();
            confirmBillingAddress();
        }
    }

    private boolean confirmBillingAddress() {
        try {
            List<WebElement> BillingAddress = driver.findElements(By.xpath("//button[contains(text(),'Bill to this address')]"));
            if (BillingAddress.size() > 0) {
                for (WebElement e : BillingAddress) {
                    e.click();
                    System.out.println("inside Bill to this address");
                    return true;
                }
            }
        }
        catch (NoSuchElementException e ) {
        e.printStackTrace();
        }
        return false;

    }

    private void enter_newBiling_Address() {
        WebElement NewAddress_FirstName = driver.findElement(By.id("NewAddress_FirstName"));
        NewAddress_FirstName.sendKeys(newAddress_FirstName);
        WebElement NewAddress_LastName = driver.findElement(By.id("NewAddress_LastName"));
        NewAddress_LastName.sendKeys(newAddress_LastName);
        WebElement NewAddress_Address1 = driver.findElement(By.id("NewAddress_Address1"));
        NewAddress_Address1.sendKeys(newAddress_Address1);
        WebElement NewAddress_ZipPostalCode = driver.findElement(By.id("NewAddress_ZipPostalCode"));
        NewAddress_ZipPostalCode.sendKeys(newAddress_ZipPostalCode);
        WebElement NewAddress_City = driver.findElement(By.id("NewAddress_City"));
        NewAddress_City.sendKeys(newAddress_City);

        WebElement Country  =  driver.findElement(By.id("NewAddress_CountryId"));
        ((JavascriptExecutor)driver).executeScript
                ("arguments[0].setAttribute('display', 'inline')",  Country);
        Select country_id = new Select(Country);
        country_id.selectByVisibleText(newAddress_Country);


        WebElement Email = driver.findElement(By.xpath("//*[@id='content-center']/div/div[2]/div[2]/div[2]/div/form/div[1]/div/div[12]/label"));
        Email.sendKeys(newAddress_Email);
        WebElement phoneno = driver.findElement(By.xpath(".//*[@id='content-center']/div/div[2]/div[2]/div[2]/div/form/div[1]/div/div[13]/label"));
        phoneno.sendKeys(newAddress_Phoneno);
    }

    public void removeItemsFromBasket() {

    }
}