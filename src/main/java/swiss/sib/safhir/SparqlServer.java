package swiss.sib.safhir;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import org.eclipse.rdf4j.http.server.readonly.QueryResponder;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import swiss.sib.swissprot.handlegraph4j.simple.SimpleEdgeHandle;
import swiss.sib.swissprot.handlegraph4j.simple.SimpleNodeHandle;
import swiss.sib.swissprot.handlegraph4j.simple.SimplePathGraph;
import swiss.sib.swissprot.handlegraph4j.simple.SimplePathHandle;
import swiss.sib.swissprot.handlegraph4j.simple.SimpleStepHandle;
import swiss.sib.swissprot.sapfhir.sparql.PathHandleGraphSail;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = { "org.eclipse.rdf4j", "swiss.sib" })
@Import(QueryResponder.class)
public class SparqlServer implements ApplicationRunner {
	private static Logger logger = LoggerFactory.getLogger(SparqlServer.class);

	@Value("${byteBuffer}")
	private String byteBuffer;

	@Value(value = "${base}")
	private String base;

	@Bean(destroyMethod = "shutDown")
	public Repository getRepository() throws IOException {
		RandomAccessFile raf = new RandomAccessFile(new File(byteBuffer), "r");
		SimplePathGraph spg = SimplePathGraph.open(raf);
		if (base == null) {
			base = "http://example.org/vg/";
		}
		PathHandleGraphSail<SimplePathHandle, SimpleStepHandle, SimpleNodeHandle, SimpleEdgeHandle> sail = new PathHandleGraphSail<>(
				spg, base);
		return new SailRepository(sail);
	}

	public static void main(String[] args) {
		SpringApplication.run(SparqlServer.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
		logger.info("NonOptionArgs: {}", args.getNonOptionArgs());
		logger.info("OptionNames: {}", args.getOptionNames());

		for (String name : args.getOptionNames()) {
			logger.info("arg-" + name + "=" + args.getOptionValues(name));
		}

		boolean containsOption = args.containsOption("byteBuffer");
		logger.info("Contains byteBuffer: " + containsOption);
	}
}
