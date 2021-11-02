package automation.library.api.core;

import static com.atlassian.oai.validator.util.StringUtils.requireNonEmpty;

import java.util.stream.Collectors;

import com.atlassian.oai.validator.SwaggerRequestResponseValidator;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.report.ValidationReport;
import com.atlassian.oai.validator.report.ValidationReport.Message;
import com.atlassian.oai.validator.restassured.RestAssuredRequest;
import com.atlassian.oai.validator.restassured.RestAssuredResponse;
import com.atlassian.oai.validator.restassured.SwaggerValidationFilter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import automation.library.common.TestContext;

/**
 * Custom Validation filter based on {@link SwaggerValidationFilter} but uses
 * {@link TestContext} for soft assertions
 * 
 * @author a.g.mcclelland
 *
 */
public class SwaggerSoftValidationFilter implements Filter {

	private String mode;
	private SwaggerRequestResponseValidator validator;

	public SwaggerSoftValidationFilter(String mode, String swaggerJsonUrl) {
		requireNonEmpty(swaggerJsonUrl, "A Swagger URL is required");
		this.mode = mode;
		this.validator = SwaggerRequestResponseValidator.createFor(swaggerJsonUrl).build();
	}

	@Override
	public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
			FilterContext ctx) {
		Response response = ctx.next(requestSpec, responseSpec);

		ValidationReport validationReport = createValidationReport(requestSpec, response);

		if (validationReport.hasErrors()) {
			validationReport.getMessages().forEach(System.err::println);

			String msg = String.format("Swagger validation errors:%n%s", validationReport.getMessages().stream()
					.map(Message::toString).collect(Collectors.joining("\n\t", "\t", "\n")));

			TestContext.getInstance().sa().fail(msg);
		}

		return response;
	}

	private ValidationReport createValidationReport(FilterableRequestSpecification requestSpec, Response response) {
		switch (mode) {
		case "request":
			return validator.validateRequest(RestAssuredRequest.of(requestSpec));
		case "response":
			return validator.validateResponse(requestSpec.getDerivedPath(),
					Request.Method.valueOf(requestSpec.getMethod()), RestAssuredResponse.of(response));
		case "request and response":
			return validator.validate(RestAssuredRequest.of(requestSpec), RestAssuredResponse.of(response));
		default:
			throw new IllegalArgumentException(String.format("Unknown Swagger validation mode: %s", mode));
		}
	}

}
