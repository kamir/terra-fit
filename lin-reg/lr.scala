import scala.util.Random
import java.util.Calendar
import java.text.SimpleDateFormat
import java.text.DateFormat
import java.io._
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.regression.LassoWithSGD
import org.apache.spark.mllib.regression.RidgeRegressionWithSGD
import org.apache.spark.mllib.regression.LinearRegressionModel
import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.mllib.optimization.{SimpleUpdater, SquaredL2Updater, L1Updater}
import org.apache.spark.mllib.optimization.LeastSquaresGradient
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.feature.StandardScaler
import org.apache.spark.mllib.util.MLUtils

//
// We create and store some example data for tests
//
def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
  val p = new java.io.PrintWriter(f)
  try { op(p) } finally { p.close() }
}

//
// We need a time label for the result file
//
val today = java.util.Calendar.getInstance().getTime()
val formatter = new SimpleDateFormat("yyyy.MM.dd-hh.mm.ss")
val t = formatter.format( today )

//
// Which file should be used for training the model?
// 
val datafile = "lin-reg/lr_3.dat"  // self made test data with fixed parameters

//
// Data for a model y(x1,x2) = m1 * x + m2 * x + n
// 
val m1 = 5.0
val m2 = 2.0
val n = 10.0

// x1 and x2 are varied
val x1 = 0 until 10
val x2 = 0 until 10
 
//
// Create a test-data file just once ...
// 
val record = x1.map{ 
	f1 => {
	   x2.map{ 
	      f2 => {
		  ( ( m1 * f1 + m2 * f2 + n) + "," + f1 + " " + f2 ) }	
		  } 
	   }
	} 
	
printToFile(new File( datafile )) { 
  p => record.foreach( p.println )
}

// val createdData = x1.map{ f1 => 
//    LabeledPoint( 
//      (m1 * f1 + m2 * f2 + n).toDouble, 
//      Vectors.dense( f1, f2 )
//    )
// }

// createdData.foreach( println )
// val createdPoints = sc.parallelize( createdData )
 
// Load and parse the data
val data = sc.textFile("file:///home/cloudera/" + datafile)
data.cache()

val parsedData = data.map { line =>
  val parts = line.split(',')
    LabeledPoint(parts(0).toDouble, Vectors.dense( 
    parts(1).split(' ')(0).toDouble,parts(1).split(' ')(1).toDouble))
    }.cache()

parsedData.collect().foreach(println)

val updater1 = new L1Updater()
val updater2 = new SquaredL2Updater()

var numIterations = 100
val step = 0.5
val regParam = 0.1
val updater = updater1  // select L1 or L2 Regularization

val algorithm = new LinearRegressionWithSGD()
algorithm.setIntercept(true) 

algorithm.optimizer.setStepSize(step)
algorithm.optimizer.setUpdater(updater)
algorithm.optimizer.setRegParam(regParam)
algorithm.optimizer.setNumIterations(numIterations)
algorithm.optimizer.setMiniBatchFraction(1.0)

val initialWeights = Vectors.dense(Array.fill(2)(scala.util.Random.nextDouble()))
val model = algorithm.run(parsedData,initialWeights)

// val initialWeights = Vectors.dense(0.0, 0.0)
// val model = algorithm.run(createdPoints,initialWeights)

println(s">>>> Model intercept: ${model.intercept}, weights: ${model.weights}")

//        
// Evaluate model on training examples and compute training error
//
//val valuesAndPreds = createdPoints.map { point =>
//  val prediction = model.predict(point.features)
//  (point.label, prediction)
//}
//val MSE = valuesAndPreds.map{case(v, p) => math.pow((v - p), 2)}.mean()

val sp = LabeledPoint( 0, Vectors.dense(42,17) )

val prediction = model.predict(sp.features)

println( "Model created : " + model )
//println( "training Mean Squared Error = " + MSE)
println( "sample point : " + sp )
println( "prediction   : " + prediction )
println( algorithm )
println( updater );