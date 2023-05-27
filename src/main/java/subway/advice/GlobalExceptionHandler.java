package subway.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.dto.exception.ErrorResponse;
import subway.exception.DistanceLessThatOneException;
import subway.exception.LineNotFoundException;
import subway.exception.LinesEmptyException;
import subway.exception.BlankNameException;
import subway.exception.SameSectionException;
import subway.exception.InvalidSectionDistanceException;
import subway.exception.StationNotConnectedException;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;
import subway.exception.UpStationNotFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(final MethodArgumentNotValidException exception) {
		String message = exception.getFieldErrors().stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.collect(Collectors.joining(", "));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.from(message, HttpStatus.BAD_REQUEST.value()));
	}

	private static ResponseEntity<ErrorResponse> responseBadRequest(final RuntimeException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.from(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}

	@ExceptionHandler(InvalidSectionDistanceException.class)
	public ResponseEntity<ErrorResponse> invalidSectionDistanceExceptionHandler(final InvalidSectionDistanceException exception) {
		return responseBadRequest(exception);
	}

	@ExceptionHandler(SameSectionException.class)
	public ResponseEntity<ErrorResponse> sameSectionExceptionHandler(final SameSectionException exception) {
		return responseBadRequest(exception);
	}

	@ExceptionHandler(BlankNameException.class)
	public ResponseEntity<ErrorResponse> blankNameExceptionHandler(final BlankNameException exception) {
		return responseBadRequest(exception);
	}

	@ExceptionHandler(DistanceLessThatOneException.class)
	public ResponseEntity<ErrorResponse> distanceLessThatOneExceptionHandler(final DistanceLessThatOneException exception) {
		return responseBadRequest(exception);
	}

	@ExceptionHandler(SectionNotFoundException.class)
	public ResponseEntity<ErrorResponse> sectionNotFoundExceptionHandler(final SectionNotFoundException exception) {
		return responseNotFound(exception);
	}

	private static ResponseEntity<ErrorResponse> responseNotFound(final RuntimeException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(ErrorResponse.from(exception.getMessage(), HttpStatus.NOT_FOUND.value()));
	}

	@ExceptionHandler(UpStationNotFoundException.class)
	public ResponseEntity<ErrorResponse> upStationNotFoundExceptionHandler(final UpStationNotFoundException exception) {
		return responseNotFound(exception);
	}

	@ExceptionHandler(StationNotConnectedException.class)
	public ResponseEntity<ErrorResponse> sectionNotConnectExceptionHandler(final StationNotConnectedException exception) {
		return responseBadRequest(exception);
	}

	@ExceptionHandler(LinesEmptyException.class)
	public ResponseEntity<ErrorResponse> linesEmptyExceptionHandler(final LinesEmptyException exception) {
		return responseBadRequest(exception);
	}

	@ExceptionHandler(LineNotFoundException.class)
	public ResponseEntity<ErrorResponse> lineNotFoundExceptionHandler(final LineNotFoundException exception) {
		return responseNotFound(exception);
	}

	@ExceptionHandler(StationNotFoundException.class)
	public ResponseEntity<ErrorResponse> stationNotFoundExceptionHandler(final StationNotFoundException exception) {
		return responseNotFound(exception);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> internalServerErrorHandler(final Exception exception) {
		Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
		logger.error("Server error : " + exception.getMessage());

		String internalServerErrorMessage = "내부 서버에 문제가 발생했습니다 잠시 후 다시 시도해주세요";
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponse.from(internalServerErrorMessage, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}
}
