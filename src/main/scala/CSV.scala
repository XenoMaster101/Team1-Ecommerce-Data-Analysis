import java.io
import java.nio.file.{Files, Paths}
import java.io.{BufferedWriter, FileWriter}
import scala.collection.mutable.ListBuffer
import scala.util.Random
import scala.io.Source

object CSV {
    def main(args:Array[String]): Unit = {

        val file = "outputs/TEAM1outputV1.csv"
        val writer = new BufferedWriter(new FileWriter(file, true))
        val random = new Random

        val order_id = (1 to 10000).toList 
        val customer_id = (1 to 10000).toSeq
        val customer_name = Source.fromFile("inputs/customer_names2.csv").getLines.toList 
        val product_id = (1 to 10000).toSeq
        val product = Source.fromFile("inputs/items.csv").getLines.toList 
        val payment_type = List("Debit Card", "Credit Card", "Mobile Payments")
        val qty = (1 to 100).toList
        val price = (1 to 1000).toList
        val datatime = List("2022-05-18 02:33", "2022-01-23 10:33" , "2022-04-07 9:30" , "2022-06-23 05:15")
        val country_city = Source.fromFile("countries_cities.csv").getLines.toList
        val ecommerce_website_name = List("Amazon.com", "Ebay.com", "Alibaba.com", "Walmart.com", "Target.com")
        val paymemt_txd_id = (1 to 10000).toSeq
        val payment_txd_success = List("Y", "N")
        val failure_reason = List("My card was closed", "I entered my card info wrong by accident", "I didn't have enough")


        for (i <- order_id){
            // Customer Names
            val cu_name = customer_name(random.nextInt(customer_name.length)) 
            val cu_Id = customer_id(random.nextInt(customer_id.length)) 

            // Products
            val prod = product(random.nextInt(product.length))
            val pro_id = product_id(random.nextInt(product_id.length))
            val pro_qty = qty(random.nextInt(qty.length))
            val pro_price = price(random.nextInt(price.length))


             
            // Time/Place
            val dt_time = datatime(random.nextInt(datatime.length)) 
            // Country -> Goes here
            val cont_cty = country_city(random.nextInt(country_city.length))

            // Site 
            val web = ecommerce_website_name(random.nextInt(ecommerce_website_name.length))

            // Payment
            val pay_id = paymemt_txd_id(random.nextInt(customer_name.length))
            val pay_type = payment_type(random.nextInt(payment_type.length))
            val pay_bool = payment_txd_success(random.nextInt(payment_txd_success.length))
            val fail = if (pay_bool == "N") {
                val fail_rand = failure_reason(random.nextInt(failure_reason.length))
                fail_rand
            }
            else{
                "Payment Successful"
            }
            
            
            writer.write(s"$i,$cu_Id,$cu_name,$pro_id,$prod,$pay_type,$pro_qty,$pro_price,$dt_time,$cont_cty,$web,$pay_id,$pay_bool,$fail \n")
        }

        writer.close()
    }
}