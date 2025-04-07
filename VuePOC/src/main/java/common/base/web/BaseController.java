package common.base.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * BaseController
 * 
 * all of controller must have to extends this BaseController 
 * in BugCourt Project
 * @since 01.20.2015
 */
public class BaseController {

	@Autowired 
	LocalValidatorFactoryBean validator;

	@Autowired
	private MessageSource messageSource;

//	/**
//	 * {@inheritDoc}
//	 */
//	public void unlock(RLock lock) {
//		lock.unlock();
//	}

	/**
	 * default layout PREPIX
	 * including resources
	 */
	protected static String PREPIX_DEFAULT = "default:";

	/**
	 * single layout PREPIX
	 * including resources
	 */
	protected static String PREPIX_SINGLE = "single:";

	/**
	 * empty layout PREPIX
	 * including resources
	 */
	protected static String PREPIX_EMPTY = "empty:";

	/**
	 * 홈 화면 레이아웃 접두사
	 * 리소스를 포함한 단일 화면 레이아웃
	 */
	protected static String PREPIX_HOME = "home:";

	/**
	 * URL base view url  PREPIX
	 * /WEB-INF/jsp 
	 */
	protected static String PREPIX_URL = "";

	/**
	 * redirect PREPIX
	 */
	protected static String PREPIX_REDIRECT = "redirect:";

	/**
	 * forward PREPIX
	 */
	protected static String PREPIX_FORWARD = "forward:";


	/**
	 * method for process Exception 
	 *
	 * @param msgKey Retrieved message's key from message Resource
	 * @return Exception Entity(Could be customized after)
	 */
	protected Exception processException(final String msgKey) {
		return processException(msgKey, new String[] {});
	}

	/**
	 * method for process Exception 
	 *
	 * @param msgKey Retrieved message's key from message Resource
	 * @param exception 발생한 Exception(내부적으로 취하고 있다가 에러핸들링시 사용)
	 * @return Exception Entity(Could be customized after)
	 */
	protected Exception processException(final String msgKey, Exception e) {
		return processException(msgKey, new String[] {}, e);
	}

	/**
	 * method for process Exception 
	 * @param msgKey Retrieved message's key from message Resource
	 * @param msgKey 메세지리소스에서 제공되는 메세지의 키값
	 * @param msgArgs msgKey의 메세지에서 변수에 취환되는 값들
	 * @return Exception Entity(Could be customized after)
	 */
	protected Exception processException(final String msgKey, final String[] msgArgs) {
		return processException(msgKey, msgArgs, null);
	}

	/**
	 * method for process Exception 
	 *
	 * @param msgKey Retrieved message's key from message Resource
	 * @param msgArgs msgKey의 메세지에서 변수에 취환되는 값들
	 * @param exception 발생한 Exception(내부적으로 취하고 있다가 에러핸들링시 사용)
	 * @return Exception EgovBizException 객체
	 */
	protected Exception processException(final String msgKey, final String[] msgArgs, final Exception e) {
		return processException(msgKey, msgArgs, e, LocaleContextHolder.getLocale());
	}

	/**
	 * Exception 발생을 위한 메소드.
	 *
	 * @param msgKey 메세지리소스에서 제공되는 메세지의 키값
	 * @param msgArgs msgKey의 메세지에서 변수에 취환되는 값들
	 * @param exception 발생한 Exception(내부적으로 취하고 있다가 에러핸들링시 사용)
	 * @param locale 명시적 국가/언어지정
	 * @return Exception EgovBizException 객체
	 */
	protected Exception processException(final String msgKey, final String[] msgArgs, final Exception e, Locale locale) {
		return processException(msgKey, msgArgs, e, locale, null);
	}

	/**
	 * Exception 발생을 위한 메소드.
	 *
	 * @param msgKey 메세지리소스에서 제공되는 메세지의 키값
	 * @param msgArgs msgKey의 메세지에서 변수에 취환되는 값들
	 * @param exception 발생한 Exception(내부적으로 취하고 있다가 에러핸들링시 사용)
	 * @param locale 명시적 국가/언어지정
	 * @param exceptionCreator 외부에서 별도의 Exception 생성기 지정
	 * @return Exception CustomException 객체
	 */
	protected Exception processException(final String msgKey, final String[] msgArgs, final Exception e, final Locale locale, ExceptionCreator exceptionCreator) {
		ExceptionCreator eC = null;
		if (exceptionCreator == null) {
			eC = new ExceptionCreator() {
				public Exception createCustomException(MessageSource messageSource) {
				    return new Exception(e);
				    //WIP have to make CustomException and replace with here
				    //@TODO jelly
//				    return new Exception(messageSource, msgKey, msgArgs, msgKey, locale, e);
				}
			};
		} else {
			eC = exceptionCreator;
		}

		return eC.createCustomException(messageSource);
	}
    //@TODO jelly
	protected interface ExceptionCreator {
		Exception createCustomException(MessageSource messageSource);
	}

//	@InitBinder
//	public void initBinder(WebDataBinder binder) throws Exception {
//
//		Object obj = binder.getTarget();
//		if (obj instanceof BaseVO && UserDetailsHelper.isAuthenticated()) {
//			BaseVO baseVO = (BaseVO) obj;
//			if(StringUtils.hasText(baseVO.getFrstRegrId()))  baseVO.setFrstRegrId(UserDetailsHelper.getUserId());
//			if(StringUtils.hasText(baseVO.getLastChgrId()))  baseVO.setLastChgrId(UserDetailsHelper.getUserId());
//
//		}
//		
//		//binder.setValidator(validator);
//		binder.initDirectFieldAccess();//Setter가 아닌 필드 직접 Access 할 수 있게 해준다.
//		binder.registerCustomEditor(String.class, new StringNullEditor());
//	}
}
