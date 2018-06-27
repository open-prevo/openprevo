package ch.prevo.open.data.api;

public class JobStart extends AbstractJobEvent {
	private PaymentInformation paymentInformation;

	public PaymentInformation getPaymentInformation() {
		return paymentInformation;
	}

	public void setPaymentInformation(PaymentInformation paymentInformation) {
		this.paymentInformation = paymentInformation;
	}
}
