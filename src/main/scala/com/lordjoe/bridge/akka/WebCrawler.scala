package com.lordjoe.bridge.akka

/**
 * com.lordjoe.bridge.akka.WebCrawler 
 * User: Steve
 * Date: 8/11/2015
 */


//import junit.framework.TestCase
//import akka.actor._
//import akka.actor.Actor._
//import akka.config.Supervision._
//import akka.routing.CyclicIterator
//import akka.routing.Routing._
//import akka.dispatch.Dispatchers
//import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy
//
///**
// * Created by Jim Plush  jimplush.com   - twitter @jimplush
// * Date: 5/20/11
// *
// * our FictionalCrawlerManager will be responsible for setting up a Supervisor who will watch over our actors
// * and make sure that if they die from an unexpected exception that they are restarted cleanly.
// *
// * It will also create our pool of available working actors and create a LoadBalancer in front of them
// *
// * If anyone sees a better way to do this, drop me a line. I'd love to see some alternatives.
// *
// */
//
//// our case class to pass to our actors
//case class CrawlCandidate(url: String)
//
//object FictionalCrawlerManager {
//
//
//  // create our supervisor so if out crawler dies we get a nice restart
//  val supervisor = Supervisor(SupervisorConfig(
//    OneForOneStrategy(
//      List(classOf[Exception]), //What exceptions will be handled
//      3, // maximum number of restart retries
//      5000 // within time in millis
//    ), Nil
//
//  ))
//
//  // let's create a pool of actors limited to the number of processors I have on the current machine
//  val numberOfProcessors = Runtime.getRuntime.availableProcessors
//
//
//  // create a list of actors that are ready to accept work, these actors will be fed in round robin style
//  val crawlingActors = for (i <- 0 until numberOfProcessors) yield {
//
//    val actor = actorOf[FictionalCrawler]
//    supervisor.link(actor)
//    actor.start()
//    actor
//  }
//
//  // sets up load balancing among the actors created above to allow multithreading
//  val loadBalancedCrawlerActors = loadBalancerActor(new CyclicIterator(crawlingActors.toList))
//
//
//  println("We're going to fire up %d actors".format(crawlingActors.size))
//
//}
//
//
//class FictionalCrawler extends Actor {
//
//  // multithread this guy -> create a pool of actors to work on the messages
//  self.dispatcher = DispatchCrawler.dispatcher
//
//  protected def receive = {
//    case CrawlCandidate(url) => println("I'm going to crawl URL: %s from Thread: %s".format(url, Thread.currentThread().getName))
//    case _ => println("unknown cmd")
//  }
//
//  override def preStart() {
//    println("Initializing Actor in thread: %s".format(Thread.currentThread().getName))
//  }
//
//
//}
//
//
//object DispatchCrawler {
//  val dispatcher = Dispatchers.newExecutorBasedEventDrivenDispatcher("URL-Crawlers")
//      .withNewThreadPoolWithLinkedBlockingQueueWithCapacity(100)
//      .setMaxPoolSize(FictionalCrawlerManager.numberOfProcessors)
//      .setCorePoolSize(2)
//      .setKeepAliveTimeInMillis(60000)
//      .setRejectionPolicy(new CallerRunsPolicy).build
//}
//
//
//class LoadBalancedTestIT extends TestCase {
//
//
//  def testCreatingLoadBalancedActors() {
//
//    val manager = FictionalCrawlerManager
//
//    // create a list of URLS we're going to crawl
//    val urls =
//      "http://techcrunch.com/2011/05/20/y-combinator-wired/" ::
//          "http://techcrunch.com/2011/05/20/ahalife-curates-and-sells-unique-hard-to-find-products-from-around-the-world/" ::
//          "http://eu.techcrunch.com/2011/05/20/twitter-sued-for-breaking-uk-super-injunction-oh-yes/" ::
//          "http://eu.techcrunch.com/2011/05/20/the-man-who-wants-your-id-myid-is-launches-beta-to-1-2m-people/" ::
//          "http://techcrunch.com/2011/05/20/hackathon-sunday/" ::
//          "http://techcrunch.com/2011/05/20/linkedin-climbs-past-100-per-share-on-second-day-of-trading/" ::
//          "http://www.mobilecrunch.com/2011/05/20/apple-strikes-back-in-amazon-app-store-tiff/" ::
//          Nil
//
//
//    urls.foreach {
//      url =>
//      // fire off a message to our loadbalancer to round robin the messages
//        manager.loadBalancedCrawlerActors ! new CrawlCandidate(url)
//    }
//
//    Thread.sleep(15)
//
//
//  }
//
//
//}