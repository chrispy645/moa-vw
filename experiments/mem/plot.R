dense_eval_time = 0
sparse_eval_time = 0
meta_eval_time = 0
dense_mem = 0
sparse_mem = 0
meta_mem = 0

for(i in 1:5) {
  dense = read.csv(paste("output-time-memory/m.txt.",i,sep=""))
  dense_eval_time = dense_eval_time + dense$evaluation.time..cpu.seconds.
  dense_mem = dense_mem + dense$jvm.memory.usage..mb.
  
  sparse = read.csv(paste("output-time-memory/m_sparse.txt.",i,sep=""))
  sparse_eval_time = sparse_eval_time + sparse$evaluation.time..cpu.seconds.
  sparse_mem = sparse_mem + sparse$jvm.memory.usage..mb
  
  meta = read.csv(paste("output-time-memory/m_meta.txt.",i,sep=""))[1:20,]
  meta_eval_time = meta_eval_time + meta$evaluation.time..cpu.seconds.
  meta_mem = meta_mem + meta$jvm.memory.usage..mb 
}
dense_eval_time = dense_eval_time / 5
dense_mem = dense_mem / 5
sparse_eval_time = sparse_eval_time / 5
sparse_mem = sparse_mem / 5
meta_eval_time = meta_eval_time / 5
meta_mem = meta_mem / 5

pdf(file="mem-and-time-paper.pdf")
par(mfrow=c(2,1))

plot(dense_eval_time, type="l", col="red", ylim=c(0,800),
     xlab="# instances (x100)", ylab="Time taken (sec)")
lines(sparse_eval_time, type="l", col="orange")
lines(meta_eval_time, type="l", col="blue")
legend("topright", c("dense", "sparse", "meta"), fill=c("red","orange","blue"))

plot(dense_mem, type="l", col="red", ylim=c(0,1000),
     xlab="# instances (x100)", ylab="JVM memory usage (mb)")
lines(sparse_mem, type="l", col="orange")
lines(meta_mem, type="l", col="blue")
legend("topright", c("dense", "sparse", "meta"), fill=c("red","orange","blue"))

dev.off()