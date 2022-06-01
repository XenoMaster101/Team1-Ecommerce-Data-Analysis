import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._


object Team1{
    def main(args:Array[String]):Unit = {
        val spark =
            SparkSession
            .builder
            .appName("SparkHelloWorld")
            .config("spark.master", "local")
            .config("spark.eventLog.enabled", "false")
            .getOrCreate()


        val team3 = spark.read
            .format("csv")
            .option("inferSchema","true")
            .option("header", "true")
            .load("inputs/ecommerce-team3.csv")


        team3.createOrReplaceTempView("market_data")

        val modifiedTime= team3.withColumn("Date",split(col("Date_Time")," ").getItem(0))
        .withColumn("Time",split(col("Date_Time"), " ")).drop("Date_Time")

        val modifiedTime1=team3.withColumn("Time",split(col("Date_Time")," ").getItem(1)).drop("Date_Time")

        modifiedTime.createOrReplaceTempView("modified_data")
        modifiedTime1.createOrReplaceTempView("modified_data1")
        
        
        //q1
        def findTopCat(spark:SparkSession): DataFrame  = {

        return spark.sql("select CountryTable.country, CatTable.product_cat, CatTable.qty2 from (select country, product_cat, max(QTY) as qty2 from (select country, product_cat, max(qty) as QTY from " +
            "(select country, product_cat, sum(qty) as qty from (select country, Product_Cat, qty from ((select * from market_data where `Payment Success` = 'Y') as M_data) ) as T group by country, product_cat) as T1 group by country, product_cat) as T2 group by product_cat, country) as CatTable inner join (select country, max(QTY) as qty2 from (select country, product_cat, max(qty) as QTY from " +
            "(select country, product_cat, sum(qty) as qty from (select country, Product_Cat, qty from ((select * from market_data where `Payment Success` = 'Y') as M_data) ) as T group by country, product_cat) as T1 group by country, product_cat) as T2 group by country) as CountryTable on CountryTable.qty2 = CatTable.qty2 order by  CatTable.qty2 desc limit 10")
        }
        

        //q1b 2nd chart for 1st question
        def findTopCat2(spark:SparkSession): DataFrame  = {

        return spark.sql("select product_cat, sum(qty) as qty from (select Product_Cat, qty from ((select * from market_data where `Payment Success` = 'Y') as M_data) as T1 where country = 'Georgia') as T2 group by product_cat order by qty desc")
        
        }

        
        //q2
        def prod_year(spark:SparkSession): DataFrame ={
            return spark.sql("SELECT Country,Product_Name, Date FROM modified_data GROUP BY Country, Product_Name, Date ORDER BY Date")
        }
        

        //q3
        def traffic_sales(spark:SparkSession): DataFrame ={
            return spark.sql("SELECT Country, City, Count(Product_Id) * sum(QTY) as Sales FROM (select * from market_data where `Payment Success` = 'Y') as M_data GROUP BY Country, City ORDER BY Sales")
        }

        //q4a
        def TimeData(spark:SparkSession):DataFrame ={
            val df2=modifiedTime1.toDF
            val df3= df2.withColumn("QTY", col("QTY").cast(IntegerType))
            df3.groupBy("Time").sum("QTY")

        }

        //q4b
        def TimeData1(spark:SparkSession): DataFrame ={
            return spark.sql("SELECT Country, Time, sum(QTY) FROM modified_data1 GROUP BY Country,Time ORDER BY sum(QTY)")

        }
        

        //q1
        val q1 = findTopCat(spark).coalesce(1)
        q1.write.mode("overwrite").csv("outputs/q1_csv3")
        q1.limit(10).show()

        val q1b = findTopCat2(spark).coalesce(1)
        q1b.write.mode("overwrite").csv("outputs/q1b_csv3")
        q1b.limit(10).show()
        

        //q2
        val product_year = prod_year(spark).coalesce(1)
        product_year.write.mode("overwrite").csv("outputs/q2_csv3")
        product_year.show()

        
        //q3
        val q3 = traffic_sales(spark).coalesce(1)
        q3.write.mode("overwrite").csv("outputs/q3_csv3")
        q3.show()
        
        
        //q4
        val q4 = TimeData(spark)
        q4.repartition(1).write.mode("overwrite").csv("outputs/q4_csv3")
        q4.show()

        //q4b
        val q4b = TimeData1(spark)
        q4b.repartition(1).write.mode("overwrite").csv("outputs/q4b_csv3")
        q4.show()
        
        spark.stop()

    }
}