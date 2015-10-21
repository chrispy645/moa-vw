postfixes = c(
  "agrawal.txt",
  "hyperplane.txt",
  "led.txt",
  "rbf.txt",
  "rtg.txt",
  "sea.txt",
  "stagger.txt",
  "waveform.txt"
)

names = c(
  "Agrawal",
  "Hyperplane",
  "LED",
  "RandomRBF",
  "RandomTree",
  "SEA",
  "STAGGER",
  "Waveform"
)

pdf("plot-a4.pdf", height=10)
par(mfrow=c(4,2))
rainbows = rainbow(7)
for(z in 1:length(postfixes)) {
  postfix=postfixes[z]
  print(postfix)
  df_sgd = read.csv(paste("output-datasets/","sgd_",postfix,sep=""))
  plot(df_sgd$classifications.correct..percent., type="l", col=rainbows[1], ylim=c(0,100), main=names[z],
       xlab="# instances (x1000)", ylab="Accuracy (%)")
  num_bits = c(3,6,9,12,15,18)
  for(i in 1:length(num_bits)) {
    tmp = read.csv(paste("output-datasets/","vw_",num_bits[i],"_oaa_",postfix,sep=""))
    lines(tmp$classifications.correct..percent., col=rainbows[i+1])
  }
  if(postfix != "led.txt") {
    legend("bottomright", fill=rainbows, cex=1,
           legend=c("sgd", "vw_b3", "vw_b6", "vw_b9", "vw_b12", "vw_b15", "vw_b18"), ncol=2)
  }
}
dev.off()

# plot time
pdf("plot-time-a4.pdf")
par(mfrow=c(4,2))
for(z in 1:length(postfixes)) {
  postfix = postfixes[z]
  df_vw = read.csv(paste("output-datasets/","vw_18_oaa_",postfix,sep=""))
  plot(df_vw$evaluation.time..cpu.seconds., type="l", col="blue", main=names[z],
       xlab="# instances (x1000)", ylab="Time (sec)")
  df_sgd = read.csv(paste("output-datasets/","sgd_",postfix,sep=""))
  lines(df_sgd$evaluation.time..cpu.seconds., col="red")
}
dev.off()


