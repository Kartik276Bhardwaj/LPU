class SQLHandler{
    // This handles all the DB side part, along with exceptions
    static Scanner sc= new Scanner(System.in);
    static Statement statement;
    static{
        try{
            // Connecting to the DB
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/alltables",
                "root" ,"1234"
                );
            statement = connection.createStatement();
        }catch(Exception x){
            System.out.println("Something Wrong Happened");
        }
    }
    // to insert into the cars table by admin
    public static void addToCars(String regNo, String chasisNo, String manufacturer, String model, int seats){
        try{
            statement.executeUpdate("insert into car values('"+regNo+"','"+chasisNo+"','"+manufacturer+"','"+model+"',"+seats+");");
            System.out.println("Success, the car is inserted into the database");
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    // to remove from cars table by admin
    public static void removeFromCars(String regNo){
        try{
            // SQL to delete from table, parameter is taken in Main
            statement.executeUpdate("delete from car where regNo='"+regNo+"';");
            System.out.println("Success, the car is deleted from the database");
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    // to show the contents of any table
    public static ArrayList<String> showTable(String table){
        try{
            // fetch data
            ResultSet result = statement.executeQuery("select * from "+table+";");
            ResultSetMetaData rsmd= result.getMetaData();
            int n= rsmd.getColumnCount();
            ArrayList<String> tableData = new ArrayList<String>();
            // put it into arraylist
            while(result.next()){
                String s="";
                for(int i=1; i<=n; i++){
                    s+=result.getString(i)+" ";
                }
                tableData.add(s);
            }
            // return it
            return tableData;
        }catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }
    public static ArrayList<String> showAvailiableCars(){
        try{
            // show the availiable cars to the customer
            ArrayList<String> availiableCars = new ArrayList<String>();
            ResultSet result = statement.executeQuery("select regno, model, seats from car where regno not in (select regNo from rent)");
            int n= result.getMetaData().getColumnCount();
            while(result.next()){
                String s="";
                for(int i=1; i<=n; i++){
                    s+=result.getString(i)+" ";
                }
                availiableCars.add(s);
            }
            return availiableCars;
        }catch(Exception x){
            System.out.println("Something went wrong, please try again");
            return null;
        }
    }
    public static void addToRent(String reg, String licence){
        try{
            statement.executeUpdate("insert into rent values('"+reg+"','"+licence+"',"+System.currentTimeMillis()/1000+");");
            System.out.println("Car successfully booked");
        }catch(SQLIntegrityConstraintViolationException icv){
            System.out.println("You might have entered wrong number, please enter it again");
        }catch(Exception x){
            System.out.println("Something wrong happened, please try again");
        }
    }
    public static long releiveCar(String reg, String licence){
        try{
            // fetch the time for which it is taken
            ResultSet result= statement.executeQuery("select * from rent where regNo='"+reg+"';");
            result.next();
            // check if the car is booked by the same person
            String lic= result.getString("plicence");
            if(!licence.equals(lic)){
                System.out.println("Looks like you didn't book the car");
                return 0;
            }
            // calculate the time in minutes
            long time= (System.currentTimeMillis()/1000 - result.getLong("tookAt"))/60;
            // remove it from the rent DB
            statement.executeUpdate("delete from rent where regNo='"+reg+"';");
            System.out.println("The car "+reg+" has been successfully relieved");
            return time;
        }catch(SQLException sql){
            if(sql.getMessage()=="Before start of result set")
                System.out.println("No such car is booked, please check if it is a typo");
        }
        return 0;
    }
    public static boolean validUser(String licence, String pass){
        try{
            ResultSet result= statement.executeQuery("select password from person where licence='"+licence+"';");
            result.next();
            return pass.equals(result.getString("password"));
        }catch(SQLException sql){
            if(sql.getMessage()=="Before start of result set")
                System.out.println("User name doesnot exist, please check if it is a typo");
        }
        return false;
    }
    public static void signUp(String name, int age, String gender, String licence, long contact, long emergencyContact, String pass){
        try{
            statement.executeUpdate("insert into person values('"+name+"',"+age+",'"+gender+"','"+licence+"','"+pass+"',"+contact+","+emergencyContact+");");
            System.out.println("Successfully SignedUp");
        }catch(SQLIntegrityConstraintViolationException sqli){
            System.out.println("The given licence already exists");
        }catch(SQLException sql){
            System.out.println("Some error occured, please try again");
        }
    }
}


class Main1{
    public static void main(String[] args) {
        String[] data= {
            "'PB19DI8487', 'JH4NA1150RT000268', 'Renault', 'Kwid', 5",
            "'PB19DI8488', '1G8ZG127XWZ157259', 'Maruti', 'Alto k10', 5",
            "'PB19DI8489', '5N3ZA0NE6AN906847', 'Hyundai', 'Santro', 5",
            "'PB19DI8490', 'JH4DA1850JS005062', 'Bajaj', 'Qute', 4",
            "'PB19DI8491', 'SCA1S684X4UX07444', 'Datsun', 'Redi-GO', 5",
            "'PB19DI8492', 'JH4DB1570DIS000858', 'Maruti', 'Alto-800', 5",
            "'PB19DI8493', 'ZCFJS7458M1953433', 'Tata', 'Tiago', 5",
            "'PB19DI8494', '1J4FA29DI4YDI728937', 'Maruti', 'S-presso', 5",
            "'PB19DI8495', '1FVACYDT19HAJ2694', 'Maruti', 'Suzuki-eco', 7",
            "'PB19DI8496', '1G8MF35X68Y131819', 'Maruti', 'Suzuki-WagonR', 5",
            "'PB19DI8497', 'JF2SHADC3DG417185', 'Mahindra', 'XUV300 Turbosport', 5",
            "'PB19DI8498', 'JT3HJ85J6T0133046', 'Maruti', 'Suzuku Grand Vitara', 5",
            "'PB19DI8499', '3VWSB81H8WM210368', 'Mahindra', 'Bolero NEo', 7",
            "'PB19DI8500', 'JH4KA8162MC010197', 'Toyota', 'Urban Cruiser', 5",
            "'PB19DI8501', 'JH4CC2560RC008414', 'Mahindra', 'Scorpio Classic', 7",
            "'PB19DI8502', 'JG1MR215XJK752025', 'Hyundai', 'i20', 5",
            "'PB19DI8503', '2CNBJ13C3Y6924710', 'Tata', 'Nexon', 5",
            "'PB19DI8504', 'JH4KA3151KC019450', 'Hyundai', 'Verna', 5",
            "'PB19DI8505', 'JN8AZ2NE5C9016953', 'Skoda', 'KushaQ', 5",
            "'PB19DI8506', 'JH4KA8170NC000665', 'Toyota', 'Innova-Crysta', 7",
            "'PB19DI8513', '5FNRL18613B046732', 'Toyota', 'Landcruiser', 8"
        };
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/alltables",
                "root" ,"1234"
                // username, password
                );
            Statement statement = connection.createStatement();
            statement.executeUpdate("create table persons(name varchar(30), age int, gender varchar(6), licence varchar(20) PRIMARY KEY, password varchar(20), contact bigint, emergencyContact bigint);");
            statement.executeUpdate("create table cars(regNo varchar(10) PRIMARY KEY, chasisNo varchar(20), manufacturer varchar(20), model varchar(30), seats int);");
            statement.executeUpdate("create table rest(regNo varchar(10) PRIMARY KEY, plicence varchar(20), tookAt bigint);");
            statement.executeUpdate("ALTER TABLE rest ADD FOREIGN KEY(regNo) REFERENCES cars(regNo);");
            statement.executeUpdate("ALTER TABLE rest ADD FOREIGN KEY(plicence) REFERENCES persons(licence);");
            for(String s: data){
                statement.executeUpdate("insert into cars values("+s+");");
            }
        }catch(Exception x){
            System.out.println(x);
        }
    }
}

