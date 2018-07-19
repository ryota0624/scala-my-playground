package validation

object Validation {
  case class Validator[S, M](private[Validation] val getResults: S => Seq[ValidationFailure[M]])
  def validator[S, T, M](validate: T => Seq[ValidationFailure[M]])(getTarget: S => T): Validator[S, M] =
    Validator(getTarget andThen validate)

  def run[S, M](validators: Seq[Validator[S, M]])(source: S): Seq[ValidationFailure[M]] =
    validators.flatMap(_.getResults(source))

  def ifTextLengthOver[M](maxLength: Int)(message: M)(text: String): Seq[ValidationFailure[M]] =
    if (text.length > maxLength) ValidationFailure(message) :: Nil
    else Nil

}

case class ValidationFailure[M](message: M)

